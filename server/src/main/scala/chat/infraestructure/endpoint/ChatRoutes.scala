package chat.infraestructure.endpoint

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import chat.domain.messages.MessageRequest
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import chat.domain.messages.AuthorFilter
import chat.domain.messages.MessageService

object ChatRoutes {

  val chatRoute = "chat"

  def roomRoutes[F[_]: Sync](chatMessages: MessageService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / chatRoute =>
        chatMessages.getAll.flatMap(msgs => Ok(msgs.asJson))
      case req @ POST -> Root / chatRoute =>
        req
          .decodeJson[MessageRequest]
          .flatMap(json => chatMessages.create(json)) *> Created()
      case req @ POST -> Root / chatRoute / "filter" =>
        req
          .decodeJson[AuthorFilter]
          .flatMap(req => chatMessages.getBy(req))
          .flatMap(msgs => Ok(msgs.asJson))
    }
  }

}
