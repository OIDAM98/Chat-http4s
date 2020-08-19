package chat.algebras

import chat.domain.users._

trait UsersAlgebra[F[_]] {
  def create(username: UserName, password: Password): F[UserId]
  def find(username: UserName, password: Password): F[Option[User]]
}
