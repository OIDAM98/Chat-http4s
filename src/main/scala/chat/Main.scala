package chat

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import scala.concurrent.ExecutionContext.global
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {
  def run(args: List[String]) =
    for {
      httpApp <- ChatServer.stream[IO]
      _ <-
        BlazeServerBuilder[IO](global)
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(httpApp)
          .serve
          .compile
          .drain
    } yield ExitCode.Success

}
