package com.atanana.vk_digest.ui

import javax.inject.Inject

import com.atanana.vk_digest.MailConfig
import com.atanana.vk_digest.vk.UsersProvider
import com.vk.api.sdk.objects.messages.Message
import com.vk.api.sdk.objects.users.User
import play.twirl.api.Html

class UiComposer @Inject()(private val config: MailConfig, private val usersProvider: UsersProvider) {

  def composeMail(messages: List[Message], users: Map[Int, User]): MailData = MailData(
    messagesHtml(messages, users),
    subject
  )

  private def messagesHtml(messages: List[Message], users: Map[Int, User]): Html = {
    val messagesHtml = html.messages(messages, users, this.messages)
    html.main(subject, messagesHtml)
  }

  private def subject: String = s"${config.subjectPrefix} ${UiUtils.subjectDate}"

  private def messages(messages: List[Message]): Html = {
    val userIds = messages.map(_.getUserId.intValue()).distinct
    val users = usersProvider.users(userIds)
    html.messages(messages, users, this.messages)
  }
}

case class MailData(html: Html, subject: String)