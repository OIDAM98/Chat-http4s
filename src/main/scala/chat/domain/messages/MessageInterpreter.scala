package chat.domain.messages

import scala.collection.mutable.ListBuffer
import cats.effect.Sync
import cats.Applicative
import cats.instances.option._
import cats.syntax.apply._

class MessageInterpreter[F[_]](private val messages: ListBuffer[Message])(implicit e: Sync[F])
    extends MessageAlgebra[F] {
  def create(by: String, txt: String): F[Unit] = {
    val newmsg = Messages.createMessage(by, txt)
    e.delay {
      newmsg match {
        case Some(msg) => messages += msg
        case None => ()
      }
    }
  }
  def getAll: F[List[Message]] = e.delay(messages.toList)
  def getBy(author: String): F[List[Message]] =
    e.delay(messages.filter(_.by.value == author).toList)
}
