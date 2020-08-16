package chat.infraestructure.endpoint

import cats.effect.{Sync, IO}
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import chat.domain.messages.MessageRequest
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.refined._
import io.circe.syntax._
import chat.domain.messages.AuthorFilter
import chat.domain.messages.MessageService
import chat.domain.messages.types._

object ChatRoutes {

  val chatRoute = "chat"

  implicit val msgJsonDec = jsonOf[IO, MessageRequest]

  def roomRoutes[F[_]: Sync](chatMessages: MessageService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / chatRoute =>
        chatMessages.getAll.flatMap(msgs => Ok(msgs.asJson))
      case req @ POST -> Root / chatRoute =>
        req
          .decodeJson[MessageRequest]
          .flatMap(json => chatMessages.create(json))
          .flatMap(res => Created(res.asJson))
      case req @ POST -> Root / chatRoute / "filter" =>
        req
          .decodeJson[AuthorFilter]
          .flatMap(req => chatMessages.getBy(req))
          .flatMap(msgs => Ok(msgs.asJson))
    }
  }

}
