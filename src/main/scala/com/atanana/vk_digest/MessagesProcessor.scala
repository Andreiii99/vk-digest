package com.atanana.vk_digest

import javax.inject.Inject

import com.atanana.vk_digest.mailers.Mailer
import com.atanana.vk_digest.ui.UiComposer
import com.atanana.vk_digest.vk.MessageProvider
import com.vk.api.sdk.objects.messages.Message

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MessagesProcessor @Inject()(
                                   private val messageProvider: MessageProvider,
                                   private val jsonStore: JsonStore,
                                   private val uiComposer: UiComposer,
                                   private val mailer: Mailer
                                 ) {
  def process(): Future[Unit] = {
    val lastMessage = jsonStore.read.map(_.lastMessage)
    val messages = messageProvider.messages(lastMessage)

    if (messages.nonEmpty) {
      sendMail(messages).map(_ => {
        storeLastMessageId(lastMessage, lastMessageId(messages))
        Future.successful()
      })
    } else {
      Future.successful()
    }
  }

  private def sendMail(messages: List[Message]) = {
    val mailData = uiComposer.composeMail(messages.reverse)
    mailer.send(mailData)
  }

  private def storeLastMessageId(oldMessageId: Option[Int], newMessageId: Int): Unit = {
    if (!oldMessageId.contains(newMessageId)) {
      jsonStore.write(Data(newMessageId))
    }
  }

  private def lastMessageId(messages: List[Message]): Int = messages.head.getId
}
