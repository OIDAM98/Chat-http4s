package chat

import cats.{ ApplicativeError, MonadError }

package object effects {
  type MonadThrow[F[_]] = MonadError[F, Throwable]
}