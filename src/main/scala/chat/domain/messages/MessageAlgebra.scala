package chat.domain.messages

trait MessageAlgebra[F[_]] {
  def create(by: String, txt: String): F[Unit]
  def getAll: F[List[Message]]
  def getBy(author: String): F[List[Message]]
}
