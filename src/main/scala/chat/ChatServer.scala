package chat

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.server.staticcontent._
import scala.concurrent.ExecutionContext.global
import chat.domain.messages.MessageService
import chat.infraestructure.endpoint._
import chat.infraestructure.repository.inmemory.MessageRepositoryInMemoryInterpreter
import cats.effect.Blocker
import org.http4s.server.Router
import java.util.concurrent.Executors

object ChatServer {

  def stream[F[_]: ContextShift: ConcurrentEffect: Timer]: Stream[F, Nothing] = {
    for {
      client <- BlazeClientBuilder[F](global).stream
      storage  = MessageRepositoryInMemoryInterpreter.empty[F]
      messages = MessageService.empty[F](storage)
      blocker = {
        val numBlockingThreads = 4
        val blockingPool       = Executors.newFixedThreadPool(numBlockingThreads)
        Blocker.liftExecutorService(blockingPool)
      }
      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = Router(
        "/" -> fileService(FileService.Config("./static", blocker)),
        "api"    -> ChatRoutes.roomRoutes[F](messages)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
