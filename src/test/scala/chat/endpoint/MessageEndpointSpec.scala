package chat.endpoint

import cats.data.NonEmptyList
import chat.domain.messages._
import chat.ChatServerArbitraries
import chat.ChatRoutes
import cats.effect._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.dsl._
import org.http4s.circe._
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.server.Router
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.matchers.should.Matchers

class MessageEndpointSpec
    extends AnyFunSuite
    with Matchers
    with ScalaCheckPropertyChecks
    with ChatServerArbitraries
    with Http4sDsl[IO]
    with Http4sClientDsl[IO] {
  implicit val msgDec: EntityDecoder[IO, chat.domain.messages.Message] = jsonOf
  implicit val msgReqEnc: EntityEncoder[IO, MessageRequest] = jsonEncoderOf

  def makeResources(): (HttpApp[IO], MessageInterpreter[IO]) = {
    val msgRepo = MessageRepository.empty[IO]
    val msgEndpoint = ChatRoutes.roomRoutes(msgRepo)
    val msgRoute = Router(("/", msgEndpoint)).orNotFound
    (msgRoute, msgRepo)
  }

  test("Get all messages") {
    val (route, messages) = makeResources()
    forAll { msg: MessageRequest =>
      (for {
        request <- POST(msg, uri"/chat")
        response <- route.run(request)
      } yield messages.getAll.unsafeRunSync() should not be empty)
    }
  }

  test("Submit a message") {
    val (route, _) = makeResources()

    forAll { msg: MessageRequest =>
      (for {
        request <- POST(msg, uri"/chat")
        response <- route.run(request)
      } yield response.status shouldEqual Created).unsafeRunSync
    }
  }

  test("Filter by User Name") {
    val (route, messages) = makeResources()
    forAll { reqmsg: MessageRequest =>
      (for {
        createReq <- POST(reqmsg, uri"/chat")
        createResp <- route.run(createReq)
        msgOpt = Messages.createMessage(reqmsg.author, reqmsg.msg)
      } yield msgOpt match {
        case Some(msg) =>
          val found = messages.getBy(msg.by.value).unsafeRunSync()
          found.contains(msg) shouldEqual true
        case _ => ()
      }).unsafeRunSync
    }
  }
}
