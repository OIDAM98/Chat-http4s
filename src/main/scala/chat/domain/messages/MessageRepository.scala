package chat.domain.messages

import scala.collection.mutable.ListBuffer
import cats.effect.Sync

object MessageRepository {
  def empty[F[_]: Sync]: MessageInterpreter[F] =
    new MessageInterpreter[F](ListBuffer.empty[Message])
}
