package chat.domain.users

import chat.domain.users.types._

trait UsersAlgebra[F[_]] {
  def create(username: UserName, password: Password): F[UserId]
}
