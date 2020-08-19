package chat.infraestructure.repository.inmemory

import chat.algebras.MessagesAlgebra
import scala.collection.mutable.ListBuffer
import chat.domain.messages.Message

import cats._
import cats.effect.Sync
import cats.implicits._
import cats.effect.concurrent.Ref

class MessagesInMemoryInterpreter[F[_]: Sync](messages: Ref[F, ListBuffer[Message]])
    extends MessagesAlgebra[F] {

  def create(newmsg: Message): F[Message] =
    messages.update(lst => lst += newmsg) *> newmsg.pure[F]

  def getAll: F[List[Message]] = messages.get.map(_.toList)
  def getBy(author: String): F[List[Message]] =
    messages.get.map(_.filter(msg => msg.by == author).toList)
}

object MessagesInMemoryInterpreter {
  def empty[F[_]: Sync](
      messages: Ref[F, ListBuffer[Message]]
  ): F[MessagesInMemoryInterpreter[F]] =
    Sync[F].delay(new MessagesInMemoryInterpreter[F](messages))
}
