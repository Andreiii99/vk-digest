package com.atanana.vk_digest

import com.vk.api.sdk.objects.messages.Message
import play.twirl.api.Html

class UiComposer {
  def composeMail(messages: List[Message]): MailData = MailData(
    messagesHtml(messages),
    subject
  )

  private def messagesHtml(messages: List[Message]): Html = {
    val messagesHtml = html.messages(messages)
    html.main(messagesHtml)
  }

  private def subject: String = ???
}

case class MailData(html: Html, subject: String)