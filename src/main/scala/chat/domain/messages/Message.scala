package chat.domain.messages

final case class Author(value: String) extends AnyVal
final case class Body(value: String) extends AnyVal

final case class AuthorFilter(by: String)
case class Message(by: Author, txt: Body)
case class MessageRequest(author: String, msg: String)

object Messages {
  import cats.instances.option._
  import cats.syntax.apply._

  def createMessage(by: String, txt: String): Option[Message] =
    (Messages.createAuthor(by), Messages.createBody(txt)).mapN(Message.apply)
  private def createAuthor(by: String): Option[Author] = Some(Author(by))
  private def createBody(txt: String): Option[Body] = Some(Body(txt))
}
