package chat

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object ChatRoutes {

  def roomRoutes[F[_]: Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / name =>
        Ok(s"Hey There ${name.capitalize}")
    }
  }

}
