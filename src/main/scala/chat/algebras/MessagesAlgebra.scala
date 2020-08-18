package chat.algebra

import chat.domain.messages.Message

trait MessagesAlgebra[F[_]] {
  def create(newmsg: Message): F[Message]
  def getAll: F[List[Message]]
  def getBy(author: String): F[List[Message]]
}
