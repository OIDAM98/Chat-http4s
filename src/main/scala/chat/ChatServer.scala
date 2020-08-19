package chat

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import org.http4s.implicits._
import org.http4s.server.middleware.Logger
import org.http4s.server.staticcontent._
import chat.infraestructure.service._
import chat.infraestructure.endpoint._
import chat.infraestructure.repository.inmemory.MessagesInMemoryInterpreter
import cats.effect.Blocker
import org.http4s.server.Router
import java.util.concurrent.Executors
import cats.effect.concurrent.Ref
import scala.collection.mutable
import chat.domain.messages.Message
import cats.effect.ExitCode

object ChatServer {

  def stream[F[_]: ContextShift: ConcurrentEffect: Timer] = {
    for {
      inmemoryDb <- Ref.of[F, mutable.ListBuffer[Message]](mutable.ListBuffer.empty[Message])
      storage    <- MessagesInMemoryInterpreter.empty[F](inmemoryDb)
      messages   <- MessageService.empty[F](storage)
      blocker = {
        val numBlockingThreads = 4
        val blockingPool       = Executors.newFixedThreadPool(numBlockingThreads)
        Blocker.liftExecutorService(blockingPool)
      }
      httpApp = Router(
        "/"   -> fileService(FileService.Config("./static", blocker)),
        "api" -> ChatRoutes.roomRoutes[F](messages)
      ).orNotFound
    } yield Logger.httpApp(true, true)(httpApp)
  }
}
