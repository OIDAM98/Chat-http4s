package chat.infraestructure.endpoint

import cats.effect.{Sync}
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
import chat.http.json._
import chat.http.decoder._

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
          .decodeR[MessageRequest] { msgreq =>
            chatMessages
              .create(msgreq)
              .flatMap(Created(_))
          }

      case req @ POST -> Root / chatRoute / "filter" =>
        req
          .decodeR[AuthorFilter]{ author =>
            chatMessages.getBy(author)
            .flatMap(Ok(_))
          }
    }
  }

}
