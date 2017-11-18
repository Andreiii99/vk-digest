package com.atanana.vk_digest.ui

import javax.inject.Inject

import com.atanana.vk_digest.MailConfig
import com.vk.api.sdk.objects.messages.Message
import com.vk.api.sdk.objects.users.User
import play.twirl.api.Html

class UiComposer @Inject()(private val config: MailConfig) {

  def composeMail(messages: List[Message], users: Map[Int, User]): MailData = MailData(
    messagesHtml(messages, users),
    subject
  )

  private def messagesHtml(messages: List[Message], users: Map[Int, User]): Html = {
    val messagesHtml = html.messages(messages)
    html.main(subject, messagesHtml)
  }

  private def subject: String = s"${config.subjectPrefix} ${UiUtils.subjectDate}"
}

case class MailData(html: Html, subject: String)