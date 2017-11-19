package com.atanana.vk_digest.ui

import com.atanana.vk_digest.MailConfig
import com.atanana.vk_digest.vk.UsersProvider
import com.vk.api.sdk.objects.messages.Message
import com.vk.api.sdk.objects.users.User
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.twirl.api.Html

class UiComposerTest extends WordSpecLike with BeforeAndAfter with MockFactory with Matchers {
  var usersProvider: UsersProvider = _
  var twirlWrapper: TwirlWrapper = _
  var composer: UiComposer = _

  before {
    usersProvider = stub[UsersProvider]
    twirlWrapper = stub[TwirlWrapper]
  }

  "UiComposer" should {
    "compose correct subject" in {
      val prefix = "test prefix"
      composer = createComposer(prefix)
      composer.composeMail(List.empty).subject shouldEqual s"$prefix ${UiUtils.subjectDate}"
    }

    "get users from messages" in {
      composer = createComposer()
      val userId = 321
      val messageId = 123
      (usersProvider.users _).when(List(userId)).returns(Map(userId -> stubUser(userId)))
      val messagesHtml = Html("test messages")
      (twirlWrapper.messages _).when(where {
        (messages, users, _) =>
          messages.map(_.getId.intValue()) == List(messageId) && users.mapValues(_.getId.intValue()) == Map(userId -> userId)
      }).returns(messagesHtml)
      val mainHtml = Html("test main")
      (twirlWrapper.main _).when(*, messagesHtml).returns(mainHtml)
      composer.composeMail(List(stubMessage(messageId, userId))).html shouldEqual mainHtml
    }

    "requests user only once" in {
      composer = createComposer()
      val userId = 321
      (usersProvider.users _).when(List(userId)).returns(Map(userId -> stubUser(userId)))
      val messagesHtml = Html("test messages")
      (twirlWrapper.messages _).when(where {
        (_, users, _) =>
          users.mapValues(_.getId.intValue()) == Map(userId -> userId)
      }).returns(messagesHtml)
      val mainHtml = Html("test main")
      (twirlWrapper.main _).when(*, messagesHtml).returns(mainHtml)
      val messages = List(stubMessage(123, userId), stubMessage(124, userId), stubMessage(125, userId))
      composer.composeMail(messages).html shouldEqual mainHtml
    }
  }

  private def stubMessage(messageId: Int, userId: Int) = {
    val message = stub[Message]
    (message.getId _).when().returns(messageId)
    (message.getUserId _).when().returns(userId)
    message
  }

  private def stubUser(userId: Int) = {
    val user = stub[User]
    (user.getId _).when().returns(userId)
    user
  }

  private def createComposer(subjectPrefix: String = "") =
    new UiComposer(config(subjectPrefix), usersProvider, twirlWrapper)

  private def config(subjectPrefix: String = "") = MailConfig("", 0, "", "", "", subjectPrefix)
}
