package chat.domain

import io.estatico.newtype.macros.newtype
import scala.language.implicitConversions
import chat.domain.users.User
import dev.profunktor.auth.jwt._

object auth {
  @newtype case class AdminJwtAuth(value: JwtSymmetricAuth)
  @newtype case class UserJwtAuth(value: JwtSymmetricAuth)

  @newtype case class CommonUser(value: User)
  @newtype case class AdminUser(value: User)
}
