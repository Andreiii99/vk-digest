package com.atanana.vk_digest.ui

import javax.inject.Inject

import com.atanana.vk_digest.MailConfig
import com.atanana.vk_digest.vk.UsersProvider
import com.vk.api.sdk.objects.messages.Message
import play.twirl.api.Html

class UiComposer @Inject()(private val config: MailConfig, private val usersProvider: UsersProvider) {

  def composeMail(messages: List[Message]): MailData = MailData(
    messagesHtml(messages),
    subject
  )

  private def messagesHtml(messages: List[Message]): Html = {
    val messagesHtml = html.messages(messages, users(messages), this.messages)
    html.main(subject, messagesHtml)
  }

  private def subject: String = s"${config.subjectPrefix} ${UiUtils.subjectDate}"

  private def messages(messages: List[Message]): Html = {
    html.messages(messages, users(messages), this.messages)
  }

  private def users(messages: List[Message]) = {
    val userIds = messages.map(_.getUserId.intValue()).distinct
    val users = usersProvider.users(userIds)
    users
  }
}

case class MailData(html: Html, subject: String)