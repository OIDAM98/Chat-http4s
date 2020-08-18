package chat.domain.messages

import cats.Monad
import cats.implicits._
import chat.algebra.MessagesAlgebra

class MessageService[F[_]: Monad](storage: MessagesAlgebra[F]) {
  def create(msgReq: MessageRequest): F[Message] = {
    val msg = msgReq.toDomain()
    storage.create(msg)
  }
  def getAll: F[List[Message]] = storage.getAll

  def getBy(author: AuthorFilter): F[List[Message]] = storage.getBy(author.by)
}

object MessageService {
  def empty[F[_]: Monad](storage: MessagesAlgebra[F]): F[MessageService[F]] = (new MessageService[F](storage)).pure[F]
}
