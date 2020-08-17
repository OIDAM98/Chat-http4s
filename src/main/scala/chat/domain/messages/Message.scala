package chat.domain.messages

import io.estatico.newtype.macros.newtype
import scala.language.implicitConversions
import chat.domain.messages.types.ValidAuthor
import chat.domain.messages.types.ValidMessage

object types {
  import eu.timepit.refined._
  import eu.timepit.refined.api.Refined
  import eu.timepit.refined.collection.{MaxSize, NonEmpty}
  import eu.timepit.refined.types.string.NonEmptyString
  import eu.timepit.refined.boolean.And
  import io.circe._
  import io.circe.generic.semiauto._
  import io.circe.refined._
  import io.estatico.newtype.ops._

  type AuthorRules = And[NonEmpty, MaxSize[W.`16`.T]]
  type AuthorT     = String Refined AuthorRules
  @newtype case class ValidAuthor(value: AuthorT)
  @newtype case class ValidMessage(value: NonEmptyString)

  implicit val validAuthorDecoder: Decoder[ValidAuthor] =
    Decoder.decodeString.emap(v => refineV[AuthorRules](v).map(_.coerce[ValidAuthor]))
  implicit val validMessageDecoder: Decoder[ValidMessage] =
    Decoder.decodeString.emap(v => refineV[NonEmpty](v).map(_.coerce[ValidMessage]))

  implicit val msgReqDecoder: Decoder[MessageRequest] =
    Decoder.forProduct2("author", "msg")(MessageRequest.apply)

  implicit val msgReqEncoder: Encoder[MessageRequest] = Encoder.forProduct2[MessageRequest, String, String]("author", "msg") { json =>
    (json.author.value.value, json.msg.value.value)
  }
}

final case class AuthorFilter(by: String)
case class Message(by: String, txt: String)

case class MessageRequest(author: ValidAuthor, msg: ValidMessage) {
  def toDomain(): Message =
    Message(author.value.value, msg.value.value)
}
