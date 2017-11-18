package com.atanana.vk_digest.ui

import javax.inject.Inject

import com.atanana.vk_digest.MailConfig
import com.vk.api.sdk.objects.messages.Message
import play.twirl.api.Html

class UiComposer @Inject()(private val config: MailConfig) {

  def composeMail(messages: List[Message]): MailData = MailData(
    messagesHtml(messages),
    subject
  )

  private def messagesHtml(messages: List[Message]): Html = {
    val messagesHtml = html.messages(messages)
    html.main(subject, messagesHtml)
  }

  private def subject: String = s"${config.subjectPrefix} ${UiUtils.subjectDate}"
}

case class MailData(html: Html, subject: String)