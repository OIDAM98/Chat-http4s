package chat.domain.messages

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import chat.algebra.MessagesAlgebra

class MessageService[F[_]: Sync: Monad](storage: MessagesAlgebra[F]) {
  def create(msgReq: MessageRequest): F[Message] = {
    val msg = msgReq.toDomain()
    storage.create(msg)
  }
  def getAll: F[List[Message]] = storage.getAll

  def getBy(author: AuthorFilter): F[List[Message]] = storage.getBy(author.by)
}

object MessageService {
  def empty[F[_]: Sync: Monad](storage: MessagesAlgebra[F]): F[MessageService[F]] =
    Sync[F].delay(new MessageService[F](storage))
}
