package chat.infraestructure.endpoint

import cats._
import cats.effect.IO
import cats.implicits._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.server.AuthMiddleware
import dev.profunktor.auth.AuthHeaders

import chat.effects._
import chat.http.json._
import chat.domain.auth.AuthAlgebra
import chat.domain.auth.ValidUsers.CommonUser
import chat.domain.users.LoginUser
import chat.domain.users.InvalidUserOrPassword
import org.http4s.circe.JsonDecoder
import chat.domain.users.CreateUser
import chat.domain.users.UserNameInUse

final class UserRoutes[F[_]: JsonDecoder: Defer: Monad: MonadThrow](
    auth: AuthAlgebra[F]
) extends Http4sDsl[F] {

  import chat.http.decoder._
  private def loginRoute: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "login" =>
        req.decodeR[LoginUser] { user =>
          auth
            .login(user.username.toDomain, user.password.toDomain)
            .flatMap(Ok(_))
            .recoverWith {
              case InvalidUserOrPassword(_) => Forbidden()
            }
        }
    }

  private def logoutRoute =
    AuthedRoutes.of[CommonUser, F] {
      case ar @ POST -> Root / "logout" as user =>
        AuthHeaders
          .getBearerToken(ar.req)
          .traverse_(t => auth.logout(t, user.value.name)) *> NoContent()
    }

  private def usersRoute =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "users" =>
        req.decodeR[CreateUser] { user =>
          auth
            .newUser(user.username.toDomain, user.password.toDomain)
            .flatMap(Created(_))
            .recoverWith {
              case UserNameInUse(u) => Conflict(u)
            }
        }
    }

  def routes(
      authMiddleWare: AuthMiddleware[F, CommonUser]
  ): HttpRoutes[F] =
    Router(
      "/auth" -> (loginRoute <+> authMiddleWare(logoutRoute) <+> usersRoute)
    )
}
