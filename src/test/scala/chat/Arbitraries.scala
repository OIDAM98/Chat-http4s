package chat

import cats.effect.IO
import org.scalacheck._
import org.scalacheck.Arbitrary.arbitrary
import chat.domain.messages._

trait ChatServerArbitraries {
  val userNameLength = 10
  val userNameGen: Gen[String] = Gen.listOfN(userNameLength, Gen.alphaChar).map(_.mkString)

  implicit val msg = Arbitrary[Message] {
    for {
      name <- userNameGen
      msg <- Gen.nonEmptyListOf(Gen.asciiPrintableChar).map(_.mkString)
    }
    yield Message(Author(name), Body(msg))
  }

  implicit val msgReq = Arbitrary[MessageRequest] {
    for {
      name <- userNameGen
      msg <- Gen.nonEmptyListOf(Gen.asciiPrintableChar).map(_.mkString)
    }
    yield MessageRequest(name, msg)
  }

  implicit val author = Arbitrary[AuthorFilter] {
    for {
      name <- userNameGen
    } yield AuthorFilter(name)
  }
}