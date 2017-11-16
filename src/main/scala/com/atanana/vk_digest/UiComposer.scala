package com.atanana.vk_digest

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

import com.vk.api.sdk.objects.messages.Message
import play.twirl.api.Html

class UiComposer {

  import UiComposer.DATE_FORMATTER

  def composeMail(messages: List[Message]): MailData = MailData(
    messagesHtml(messages),
    subject
  )

  private def messagesHtml(messages: List[Message]): Html = {
    val messagesHtml = html.messages(messages)
    html.main(messagesHtml)
  }

  private def subject: String = LocalDate.now().format(DATE_FORMATTER)
}

object UiComposer {
  val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru"))
}

case class MailData(html: Html, subject: String)