package chat.http

import cats.Applicative
import dev.profunktor.auth.jwt.JwtToken
import io.circe._
import io.circe.generic.semiauto._
import io.circe.refined._
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import eu.timepit.refined.collection.NonEmpty
import chat.domain.users._
import chat.domain.messages.types._
import chat.domain.users.types._
import chat.domain.messages._

import eu.timepit.refined._
import org.http4s.EntityDecoder
import dev.profunktor.auth.jwt.JwtToken

object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_]: Applicative, A: Encoder]: EntityEncoder[F, A] =
    jsonEncoderOf[F, A]
}
private[http] trait JsonCodecs {
  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] =
    Decoder[B].map(_.coerce[A])

  implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] =
    Encoder[B].contramap(_.asInstanceOf[B])

  implicit def coercibleKeyDecoder[A: Coercible[B, *], B: KeyDecoder]: KeyDecoder[A] =
    KeyDecoder[B].map(_.coerce[A])

  implicit def coercibleKeyEncoder[A: Coercible[B, *], B: KeyEncoder]: KeyEncoder[A] =
    KeyEncoder[B].contramap[A](_.asInstanceOf[B])

  implicit val createUserDecoder: Decoder[CreateUser] = deriveDecoder[CreateUser]
  implicit val loginUserDecoder: Decoder[LoginUser]   = deriveDecoder[LoginUser]
  implicit val msgReqDecoder: Decoder[MessageRequest] = deriveDecoder[MessageRequest]
  implicit val msgReqEncoder: Encoder[MessageRequest] = deriveEncoder[MessageRequest]
  implicit val userEnc: Encoder[User]                 = deriveEncoder[User]
  implicit val tokenEncoder: Encoder[JwtToken]        = Encoder.forProduct1("access_token")(_.value)

}
