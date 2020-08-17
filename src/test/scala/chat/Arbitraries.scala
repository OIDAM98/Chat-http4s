package chat

import org.scalacheck._
import org.scalacheck.Arbitrary.arbitrary
import chat.domain.messages._
import chat.domain.messages.types._
import eu.timepit.refined._
import io.estatico.newtype.ops._
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.api.Refined
import io.estatico.newtype.ops._
import io.estatico.newtype.Coercible

trait ChatServerArbitraries {
  val genNonEmptyString: Gen[String] =
    Gen
      .chooseNum(5, 16)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  implicit val msgReq = Arbitrary[MessageRequest] {
    for {
      name <- genNonEmptyString
      msg  <- Gen.nonEmptyListOf(Gen.asciiPrintableChar).map(_.mkString)
      validName = refineV[AuthorRules](name).map(_.coerce[ValidAuthor]).toOption.get
      validMsg  = refineV[NonEmpty](msg).map(_.coerce[ValidMessage]).toOption.get
      // TO-DO  Find better alternative for this
    } yield MessageRequest(validName, validMsg)
  }

  implicit val author = Arbitrary[AuthorFilter] {
    for {
      name <- genNonEmptyString
    } yield AuthorFilter(name)
  }
}
