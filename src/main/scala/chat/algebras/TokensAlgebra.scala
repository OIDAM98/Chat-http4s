package chat.algebras

import cats.effect.Sync
import cats.implicits._
import dev.profunktor.auth.jwt._
import io.circe.syntax._
import pdi.jwt._
import scala.concurrent.duration.FiniteDuration
import chat.config.data._
import chat.algebras.GenUUID

trait TokensAlgebra[F[_]] {
  def create: F[JwtToken]
}

final class TokensInterpreter[F[_]: GenUUID: Sync] private (
    config: JwtSecretKeyConfig,
    exp: FiniteDuration
)(implicit val ev: java.time.Clock)
    extends TokensAlgebra[F] {
  def create: F[JwtToken] =
    for {
      uuid  <- GenUUID[F].make
      claim <- Sync[F].delay(JwtClaim(uuid.asJson.noSpaces).issuedNow.expiresIn(exp.toMillis))
      secretKey = JwtSecretKey(config.value.value.value)
      token <- jwtEncode[F](claim, secretKey, JwtAlgorithm.HS256)
    } yield token
}

object TokensInterpreter {
  def make[F[_]: Sync](
      tokenConfig: JwtSecretKeyConfig,
      tokenExpiration: TokenExpiration
  ): F[TokensInterpreter[F]] =
    Sync[F].delay(java.time.Clock.systemUTC()).map { implicit jClock =>
      new TokensInterpreter[F](tokenConfig, tokenExpiration.value)
    }
}
