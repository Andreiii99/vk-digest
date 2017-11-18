package com.atanana.vk_digest

import com.atanana.vk_digest.mailers.Mailer
import com.atanana.vk_digest.ui.{MailData, UiComposer}
import com.atanana.vk_digest.vk.MessageProvider
import com.vk.api.sdk.objects.messages.Message
import com.vk.api.sdk.objects.users.User
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import play.twirl.api.Html

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

class MessagesProcessorTest extends WordSpecLike with BeforeAndAfter with MockFactory with Matchers {
  var messageProviderStub: MessageProvider = _
  var messageProviderMock: MessageProvider = _
  var storeMock: JsonStore = _
  var storeStub: JsonStore = _
  var composer: UiComposer = _
  var mailerMock: Mailer = _
  var mailerStub: Mailer = _
  var processor: MessagesProcessor = _

  before {
    messageProviderStub = stub[MessageProvider]
    messageProviderMock = mock[MessageProvider]
    storeMock = mock[JsonStore]
    storeStub = stub[JsonStore]
    composer = stub[UiComposer]
    mailerMock = mock[Mailer]
    mailerStub = stub[Mailer]
  }

  "MessagesProcessor" should {
    "read last message id from store" in {
      processor = new MessagesProcessor(messageProviderMock, storeStub, composer, mailerMock)

      val messageId = 123
      stubStoreData(Some(Data(messageId)))
      checkMessageId(Some(messageId))

      process
    }

    "read empty last message id from store" in {
      processor = new MessagesProcessor(messageProviderMock, storeStub, composer, mailerMock)

      stubStoreData(None)
      checkMessageId(None)

      process
    }

    "send mail when messages not empty" in {
      processor = new MessagesProcessor(messageProviderStub, storeStub, composer, mailerMock)
      stubStoreData(None)
      val messages = List(stubMessage(123))
      val mailData: MailData = stubMessagesMail(messages)
      checkMail(mailData)

      process
    }

    "not send mail when messages are empty" in {
      processor = new MessagesProcessor(messageProviderStub, storeStub, composer, mailerMock)
      stubStoreData(None)
      stubMessagesMail(List.empty)
      (mailerMock.send _).expects(*).never()

      process
    }

    "should save new last message id when no previous message id" in {
      processor = new MessagesProcessor(messageProviderStub, storeMock, composer, mailerStub)
      mockStoreData(None)
      val messageId = 123
      stubMessagesMail(List(stubMessage(messageId)))
      stubMailSend()
      (storeMock.write _).expects(Data(messageId))

      process
    }

    "should save last message id" in {
      processor = new MessagesProcessor(messageProviderStub, storeMock, composer, mailerStub)
      mockStoreData(None)
      val messageId = 123
      stubMessagesMail(List(stubMessage(messageId), stubMessage(321)))
      stubMailSend()
      (storeMock.write _).expects(Data(messageId))

      process
    }

    "should save new last message id when previous message id differs" in {
      processor = new MessagesProcessor(messageProviderStub, storeMock, composer, mailerStub)
      mockStoreData(Some(Data(321)))
      val messageId = 123
      stubMessagesMail(List(stubMessage(messageId)))
      stubMailSend()
      (storeMock.write _).expects(Data(messageId))

      process
    }

    "should not save last message id when no messages" in {
      processor = new MessagesProcessor(messageProviderStub, storeMock, composer, mailerStub)
      mockStoreData(Some(Data(321)))
      stubMessagesMail(List.empty)
      (storeMock.write _).expects(*).never()

      process
    }

    "should not save new last message id when sending mail failed" in {
      processor = new MessagesProcessor(messageProviderStub, storeMock, composer, mailerStub)
      mockStoreData(None)
      val messageId = 123
      stubMessagesMail(List(stubMessage(messageId)))
      stubMailSend(Future.failed(new RuntimeException()))
      (storeMock.write _).expects(*).never()

      process
    }
  }

  private def stubMailSend(result: Future[Unit] = Future.successful()) = {
    (mailerStub.send _).when(*).returns(Future.successful())
  }

  private def checkMail(mailData: MailData) = {
    (mailerMock.send _).expects(mailData).returns(Future.successful())
  }

  private def stubMessage(messageId: Int, userId: Int = 0) = {
    val message = stub[Message]
    (message.getId _).when().returns(messageId)
    (message.getUserId _).when().returns(userId)
    message
  }

  private def stubMessagesMail(messages: List[Message], users: Map[Int, User] = Map.empty) = {
    (messageProviderStub.messages _).when(*).returns(messages)
    val mailData = MailData(Html(""), "")
    (composer.composeMail _).when(where {
      messagesArg: List[Message] => messages.reverse.map(_.getId) == messagesArg.map(_.getId)
    }).returns(mailData)
    mailData
  }

  private def stubStoreData(data: Option[Data]): Any = {
    (storeStub.read _).when().returns(data)
  }

  private def mockStoreData(data: Option[Data]): Any = {
    (storeMock.read _).expects().returns(data)
  }

  private def checkMessageId(message: Option[Int]) = {
    (messageProviderMock.messages _).expects(message).returns(List.empty)
  }

  private def process = {
    Await.ready(processor.process(), 1 second)
  }
}
