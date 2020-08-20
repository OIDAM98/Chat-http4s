package chat.infraestructure.repository.inmemory

import cats._
import cats.effect.Sync
import cats.implicits._
import dev.profunktor.auth.jwt.JwtToken
import io.circe.syntax._
import io.circe.parser.decode
import pdi.jwt.JwtClaim

import cats.effect.concurrent.Ref
import chat.algebras.{AuthAlgebra, GenUUID, TokensAlgebra, UsersAlgebra}
import chat.domain.users.{Password, UserName}
import scala.collection.mutable.HashMap
import chat.domain.users.UserNameInUse
import chat.effects._
import chat.domain.users.User
import chat.http.json._
import chat.domain.users.InvalidUserOrPassword

final class AuthInMemmoryInterpreter[F[_]: GenUUID: MonadThrow] private (
    users: UsersAlgebra[F],
    tokens: TokensAlgebra[F],
    cache: Ref[F, HashMap[String, String]]
) extends AuthAlgebra[F] {
  def newUser(username: UserName, password: Password): F[JwtToken] =
    users.find(username, password).flatMap {
      case Some(_) => UserNameInUse(username).raiseError[F, JwtToken]
      case None =>
        for {
          id    <- users.create(username, password)
          token <- tokens.create
          _     <- cache.update(hs => hs.addOne((username.value, token.value)))
        } yield token
    }

  def login(username: UserName, password: Password): F[JwtToken] =
    users.find(username, password).flatMap {
      case None => InvalidUserOrPassword(username).raiseError[F, JwtToken]
      case Some(_) =>
        cache.get.flatMap { hs =>
          hs.get(username.value) match {
            case Some(t) => JwtToken(t).pure[F]
            case None =>
              tokens.create.flatMap { created =>
                cache.update(hs => hs.addOne(username.value, created.value)) *>
                  created.pure[F]
              }
          }

        }
    }

  def logout(token: JwtToken, username: UserName): F[Unit] =
    cache.update(hs => hs -= username.value)

}

object AuthInMemmoryInterpreter {
  def empty[F[_]: Sync](
      users: UsersAlgebra[F],
      tokens: TokensAlgebra[F],
      cache: Ref[F, HashMap[String, String]]
  ): F[AuthInMemmoryInterpreter[F]] =
    Sync[F].delay(new AuthInMemmoryInterpreter[F](users, tokens, cache))
}
