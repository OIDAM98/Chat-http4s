package chat.algebras

import chat.domain.users.types._
import chat.domain.users.User

trait UsersAlgebra[F[_]] {
  def create(username: UserName, password: Password): F[UserId]
  def find(username: UserName, password: Password): F[Option[User]]
}
