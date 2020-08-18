package chat.http

import cats.effect.IO
import cats.Applicative
import io.circe._
import io.circe.generic.semiauto._
import io.circe.refined._
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._
import eu.timepit.refined.collection.NonEmpty

import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import chat.domain.users._
import chat.domain.messages.types._
import chat.domain.users.types._
import chat.domain.messages._

import eu.timepit.refined._
import org.http4s.EntityDecoder
import dev.profunktor.auth.jwt.JwtToken


object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_]: Applicative, A: Encoder]: EntityEncoder[F, A] =
    jsonEncoderOf[F, A]
}
private[http] trait JsonCodecs {
  implicit val userNameParamDecoder: Decoder[UserNameParam] =
    Decoder.decodeString.emap(v => refineV[UserNameRules](v).map(_.coerce[UserNameParam]))
  implicit val validMessageDecoder: Decoder[ValidMessage] =
    Decoder.decodeString.emap(v => refineV[NonEmpty](v).map(_.coerce[ValidMessage]))
  implicit val passwordDecoder: Decoder[PasswordParam] =
    Decoder.decodeString.emap(v => refineV[NonEmpty](v).map(_.coerce[PasswordParam]))
  implicit val msgReqDecoder: Decoder[MessageRequest] =
    Decoder.forProduct2("author", "msg")(MessageRequest.apply)
  implicit val createUserDecoder: Decoder[CreateUser] = deriveDecoder[CreateUser]
  implicit val loginUserDecoder: Decoder[LoginUser]   = deriveDecoder[LoginUser]
  implicit val msgReqEncoder: Encoder[MessageRequest] =
    Encoder.forProduct2[MessageRequest, String, String]("author", "msg") { json =>
      (json.author.value.value, json.msg.value.value)
    }

  implicit val userNameEnc: Encoder[UserName] = Encoder.forProduct1("username")(_.value)
  implicit val tokenEncoder: Encoder[JwtToken] = Encoder.forProduct1("access_token")(_.value)

}
