package com.atanana.vk_digest

import javax.inject.Inject

import com.atanana.vk_digest.mailers.Mailer
import com.atanana.vk_digest.vk.MessageProvider
import com.vk.api.sdk.objects.messages.Message

class MessagesProcessor @Inject()(
                                   private val messageProvider: MessageProvider,
                                   private val jsonStore: JsonStore,
                                   private val uiComposer: UiComposer,
                                   private val mailer: Mailer
                                 ) {
  def process(): Unit = {
    val lastMessage = jsonStore.read.map(_.lastMessage)
    val messages = messageProvider.messages(lastMessage)

    if (messages.nonEmpty) {
      sendMail(messages)
      storeLastMessageId(lastMessage, lastMessageId(messages))
    }
  }

  private def sendMail(messages: List[Message]) = {
    val mailData = uiComposer.composeMail(messages)
    mailer.send(mailData)
  }

  private def storeLastMessageId(oldMessageId: Option[Int], newMessageId: Int): Unit = {
    if (!oldMessageId.contains(newMessageId)) {
      jsonStore.write(Data(newMessageId))
    }
  }

  private def lastMessageId(messages: List[Message]): Int = messages.head.getId
}
