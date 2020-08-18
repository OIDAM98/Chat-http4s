package chat.infraestructure.repository.inmemory

import chat.domain.messages.MessageRepositoryAlgebra
import scala.collection.mutable.ListBuffer
import chat.domain.messages.Message

import cats._
import cats.effect.Sync
import cats.implicits._
import cats.effect.concurrent.Ref

class MessageRepositoryInMemoryInterpreter[F[_]: Sync](messages: Ref[F, ListBuffer[Message]])
    extends MessageRepositoryAlgebra[F] {

  def create(newmsg: Message): F[Message] =
    messages.update(lst => lst += newmsg) *> newmsg.pure[F]

  def getAll: F[List[Message]] = messages.get.map(_.toList)
  def getBy(author: String): F[List[Message]] =
    messages.get.map(_.filter(msg => msg.by == author).toList)
}

object MessageRepositoryInMemoryInterpreter {
  def empty[F[_]: Sync](
      messages: Ref[F, ListBuffer[Message]]
  ): F[MessageRepositoryInMemoryInterpreter[F]] =
    (new MessageRepositoryInMemoryInterpreter[F](messages)).pure[F]
}
