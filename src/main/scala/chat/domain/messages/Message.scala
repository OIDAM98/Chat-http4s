package chat.domain.messages

final case class Author(value: String) extends AnyVal
final case class Body(value: String) extends AnyVal

final case class AuthorFilter(by: String)
case class Message(by: Author, txt: Body)
case class MessageRequest(author: String, msg: String)

object Messages {
  def createAuthor(by: String): Option[Author] = Some(Author(by))
  def createBody(txt: String): Option[Body] = Some(Body(txt))
}
