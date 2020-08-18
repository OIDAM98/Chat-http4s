package chat.domain.auth

import chat.domain.users.types.UserName
import chat.domain.users.types.Password
import dev.profunktor.auth.jwt.JwtToken

trait AuthAlgebra[F[_]] {
  def newUser(username: UserName, password: Password): F[JwtToken]
  def login(username: UserName, password: Password): F[JwtToken]
  def logout(token: JwtToken, username: UserName): F[Unit]
}
