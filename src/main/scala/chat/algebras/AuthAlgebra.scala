package chat.algebras

import chat.domain.users.UserName
import chat.domain.users.Password
import dev.profunktor.auth.jwt.JwtToken

trait AuthAlgebra[F[_]] {
  def newUser(username: UserName, password: Password): F[JwtToken]
  def login(username: UserName, password: Password): F[JwtToken]
  def logout(token: JwtToken, username: UserName): F[Unit]
}
