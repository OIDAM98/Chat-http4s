package chat.domain

import io.estatico.newtype.macros.newtype
import scala.language.implicitConversions
import chat.domain.messages.ValidMessage
import chat.domain.users.{UserName, UserNameParam, UserNameRules}

object messages {
  import eu.timepit.refined._
  import eu.timepit.refined.api.Refined
  import eu.timepit.refined.types.string.NonEmptyString

  @newtype case class ValidMessage(value: NonEmptyString)
  final case class AuthorFilter(by: String)
  case class Message(by: String, txt: String)

  case class MessageRequest(author: UserNameParam, msg: ValidMessage) {
    def toDomain(): Message =
      Message(author.value.value, msg.value.value)
  }
}
