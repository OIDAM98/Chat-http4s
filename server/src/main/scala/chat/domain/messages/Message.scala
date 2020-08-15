package chat.domain.messages

final case class Author(value: String) extends AnyVal
final case class Body(value: String)   extends AnyVal

final case class AuthorFilter(by: String)
case class Message(by: Author, txt: Body)
case class MessageRequest(author: String, msg: String)

object Messages {
  import cats.instances.tuple._
  import cats.syntax.apply._

  def createMessage(by: String, txt: String): Message =
    Message(Messages.createAuthor(by), Messages.createBody(txt))
  private def createAuthor(by: String): Author = Author(by)
  private def createBody(txt: String): Body    = Body(txt)
}
