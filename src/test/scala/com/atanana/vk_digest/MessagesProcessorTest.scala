package com.atanana.vk_digest

import com.atanana.vk_digest.vk.MessageProvider
import com.vk.api.sdk.objects.messages.Message
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers, WordSpecLike}
import play.twirl.api.Html

class MessagesProcessorTest extends WordSpecLike with BeforeAndAfter with MockFactory with Matchers {
  var providerStub: MessageProvider = _
  var providerMock: MessageProvider = _
  var storeMock: JsonStore = _
  var storeStub: JsonStore = _
  var composer: UiComposer = _
  var mailerMock: Mailer = _
  var mailerStub: Mailer = _
  var processor: MessagesProcessor = _

  before {
    providerStub = stub[MessageProvider]
    providerMock = mock[MessageProvider]
    storeMock = mock[JsonStore]
    storeStub = stub[JsonStore]
    composer = stub[UiComposer]
    mailerMock = mock[Mailer]
    mailerStub = stub[Mailer]
  }

  "MessagesProcessor" should {
    "read last message id from store" in {
      processor = new MessagesProcessor(providerMock, storeStub, composer, mailerMock)

      val messageId = 123
      stubStoreData(Some(Data(messageId)))
      checkMessageId(Some(messageId))

      processor.process()
    }

    "read empty last message id from store" in {
      processor = new MessagesProcessor(providerMock, storeStub, composer, mailerMock)

      stubStoreData(None)
      checkMessageId(None)

      processor.process()
    }

    "send mail when messages not empty" in {
      processor = new MessagesProcessor(providerStub, storeStub, composer, mailerMock)
      stubStoreData(None)
      val messages = List(stub[Message])
      val mailData: MailData = stubMessagesMail(messages)
      (mailerMock.send _).expects(mailData)

      processor.process()
    }

    "not send mail when messages are empty" in {
      processor = new MessagesProcessor(providerStub, storeStub, composer, mailerMock)
      stubStoreData(None)
      stubMessagesMail(List.empty)
      (mailerMock.send _).expects(*).never()

      processor.process()
    }

    "should save new last message id when no previous message id" in {
      processor = new MessagesProcessor(providerStub, storeMock, composer, mailerStub)
      mockStoreData(None)
      val messageId = 123
      stubMessagesMail(List(stubMessage(messageId)))
      (storeMock.write _).expects(Data(messageId))

      processor.process()
    }

    "should save last message id" in {
      processor = new MessagesProcessor(providerStub, storeMock, composer, mailerStub)
      mockStoreData(None)
      val messageId = 123
      stubMessagesMail(List(stubMessage(messageId), stubMessage(321)))
      (storeMock.write _).expects(Data(messageId))

      processor.process()
    }

    "should save new last message id when previous message id differs" in {
      processor = new MessagesProcessor(providerStub, storeMock, composer, mailerStub)
      mockStoreData(Some(Data(321)))
      val messageId = 123
      stubMessagesMail(List(stubMessage(messageId)))
      (storeMock.write _).expects(Data(messageId))

      processor.process()
    }

    "should not save last message id when no messages" in {
      processor = new MessagesProcessor(providerStub, storeMock, composer, mailerStub)
      mockStoreData(Some(Data(321)))
      stubMessagesMail(List.empty)
      (storeMock.write _).expects(*).never()

      processor.process()
    }

    def stubMessage(messageId:Int) = {
      val message = stub[Message]
      (message.getId _).when().returns(messageId)
      message
    }

    def stubMessagesMail(messages: List[Message]) = {
      (providerStub.messages _).when(*).returns(messages)
      val mailData = MailData(Html(""), "")
      (composer.composeMail _).when(messages).returns(mailData)
      mailData
    }

    def stubStoreData(data: Option[Data]): Any = {
      (storeStub.read _).when().returns(data)
    }

    def mockStoreData(data: Option[Data]): Any = {
      (storeMock.read _).expects().returns(data)
    }

    def checkMessageId(message: Option[Int]) = {
      (providerMock.messages _).expects(message).returns(List.empty)
    }
  }
}
