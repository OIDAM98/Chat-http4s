package chat.domain.users

import io.estatico.newtype.macros.newtype
import java.util.UUID
import scala.language.implicitConversions
import javax.crypto.Cipher
import eu.timepit.refined.types.string.NonEmptyString

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.{MaxSize, NonEmpty}
import eu.timepit.refined.boolean.And

object types {

  @newtype case class UserId(value: UUID)
  @newtype case class UserName(value: String)
  @newtype case class Password(value: String)

  // --------- User creation
  type UserNameRules = And[NonEmpty, MaxSize[W.`16`.T]]
  type UserNameT     = String Refined UserNameRules
  @newtype case class UserNameParam(value: UserNameT) {
    def toDomain: UserName = UserName(value.value)
  }
  @newtype case class PasswordParam(value: NonEmptyString) {
    def toDomain: Password = Password(value.value)
  }

  @newtype case class EncryptedPassword(value: String)

  @newtype case class EncryptCipher(value: Cipher)
  @newtype case class DecryptCipher(value: Cipher)

}
