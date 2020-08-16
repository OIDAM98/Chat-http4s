package chat.infraestructure.repository.inmemory

import chat.domain.messages.MessageRepositoryAlgebra
import scala.collection.mutable.ListBuffer
import chat.domain.messages.Message

import cats._
import cats.effect.Sync
import cats.implicits._

class MessageRepositoryInMemoryInterpreter[F[_]]()(implicit e: Sync[F])
    extends MessageRepositoryAlgebra[F] {

  private val messages: ListBuffer[Message] = ListBuffer.empty[Message]

  def create(newmsg: Message): F[Message] =
    e.delay {
      messages += newmsg
      newmsg
    }
  def getAll: F[List[Message]] = e.delay(messages.toList)
  def getBy(author: String): F[List[Message]] =
    e.delay(messages.filter(_.by == author).toList)
}

object MessageRepositoryInMemoryInterpreter {
  def empty[F[_]: Sync]: MessageRepositoryInMemoryInterpreter[F] =
    new MessageRepositoryInMemoryInterpreter[F]()
}
