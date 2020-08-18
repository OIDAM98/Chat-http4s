package chat.domain.messages

import io.estatico.newtype.macros.newtype
import scala.language.implicitConversions
import chat.domain.messages.types.ValidMessage
import chat.domain.users.types.{UserName, UserNameParam, UserNameRules}

object types {
  import eu.timepit.refined._
  import eu.timepit.refined.api.Refined
  import eu.timepit.refined.types.string.NonEmptyString

  @newtype case class ValidMessage(value: NonEmptyString)
}

final case class AuthorFilter(by: String)
case class Message(by: String, txt: String)

case class MessageRequest(author: UserNameParam, msg: ValidMessage) {
  def toDomain(): Message =
    Message(author.value.value, msg.value.value)
}
