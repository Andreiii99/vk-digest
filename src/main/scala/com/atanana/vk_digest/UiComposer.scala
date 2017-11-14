package com.atanana.vk_digest

import javax.inject.Inject

import com.atanana.vk_digest.vk.MessageProvider
import play.twirl.api.Html

class UiComposer @Inject()(messageProvider: MessageProvider, dataOption: Option[Data]) {
  def getMessagesHtml: Html = {
    val messages = messageProvider.messages(dataOption.map(_.lastMessage))
    val messagesHtml = html.messages(messages)
    html.main(messagesHtml)
  }
}
