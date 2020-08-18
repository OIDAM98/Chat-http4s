package chat.domain.messages

import cats.Monad
import cats.implicits._

class MessageService[F[_]: Monad](storage: MessageRepositoryAlgebra[F]) {
  def create(msgReq: MessageRequest): F[Message] = {
    val msg = msgReq.toDomain()
    storage.create(msg)
  }
  def getAll: F[List[Message]] = storage.getAll

  def getBy(author: AuthorFilter): F[List[Message]] = storage.getBy(author.by)
}

object MessageService {
  def empty[F[_]: Monad](storage: MessageRepositoryAlgebra[F]): F[MessageService[F]] = (new MessageService[F](storage)).pure[F]
}
