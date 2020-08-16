package chat.domain.messages

trait MessageRepositoryAlgebra[F[_]] {
  def create(newmsg: Message): F[Message]
  def getAll: F[List[Message]]
  def getBy(author: String): F[List[Message]]
}
