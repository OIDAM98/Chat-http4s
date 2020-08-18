package chat.domain.users

import chat.domain.users.types.UserId
import chat.domain.users.types.UserName
import chat.domain.users.types.UserNameParam
import chat.domain.users.types.PasswordParam
import scala.util.control.NoStackTrace

case class User(id: UserId, name: UserName)

case class CreateUser(username: UserNameParam, password: PasswordParam)
case class LoginUser(username: UserNameParam, password: PasswordParam)

// Errors ADT
case class UserNameInUse(username: UserName) extends NoStackTrace
case class InvalidUserOrPassword(username: UserName) extends NoStackTrace
case object UnsupportedOperation extends NoStackTrace
case object TokenNotFound extends NoStackTrace