package chat.config

import scala.language.implicitConversions
import ciris._
import scala.concurrent.duration._
import io.estatico.newtype.macros.newtype
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.types.net.UserPortNumber

object data {
  @newtype case class AdminUserTokenConfig(value: Secret[NonEmptyString])
  @newtype case class JwtSecretKeyConfig(value: Secret[NonEmptyString])
  @newtype case class JwtClaimConfig(value: Secret[NonEmptyString])
  @newtype case class TokenExpiration(value: FiniteDuration)
}
