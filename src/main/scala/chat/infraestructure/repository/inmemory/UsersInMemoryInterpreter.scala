package chat.infraestructure.repository.inmemory

import chat.algebras.GenUUID
import chat.effects.MonadThrow
import cats.effect.concurrent.Ref
import scala.collection.concurrent.TrieMap
import chat.algebras.UsersAlgebra
import chat.domain.users.{User, UserName}
import chat.domain.users.Password
import chat.domain.users.UserId
import java.util.UUID
import cats.implicits._
import cats.Monad
import cats.effect.Sync

final class UsersInMemoryInterpreter[F[_]: GenUUID: Monad] private (
    db: Ref[F, TrieMap[UUID, (User, Password)]]
) extends UsersAlgebra[F] {

  def create(username: UserName, password: Password): F[UserId] =
    for {
      uuid <- GenUUID[F].make[UserId]
      user = User(uuid, username)
      _ <- db.update(_ += (uuid.value -> (user, password)))
    } yield user.id

  def find(username: UserName, password: Password): F[Option[User]] =
    db.get.flatMap { storage =>
      storage
        .filter {
          case (_, (user, pswd)) =>
            user.name.value == username.value && pswd.value == password.value
        }
        .headOption
        .map {
          case (_, (user, _)) => user
        }
        .pure[F]
    }
}

object UsersInMemoryInterpreter {
  def empty[F[_]: Sync](
      db: Ref[F, TrieMap[UUID, (User, Password)]]
  ): F[UsersInMemoryInterpreter[F]] =
    Sync[F].delay(new UsersInMemoryInterpreter[F](db))
}
