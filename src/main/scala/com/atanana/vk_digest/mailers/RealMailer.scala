package com.atanana.vk_digest.mailers

import javax.inject.Inject
import javax.mail.internet.MimeBodyPart

import com.atanana.vk_digest.MailConfig
import com.atanana.vk_digest.ui.MailData
import courier.Defaults._
import courier._

import scala.concurrent.Future

class RealMailer @Inject()(private val config: MailConfig) extends Mailer {
  def send(mailData: MailData): Future[Unit] = {
    val mailer = Mailer(config.server, config.port)
      .auth(true)
      .as(config.address, config.password)
      .startTtls(true)()

    mailer(
      Envelope.from(config.address.addr)
        .bcc(config.addressTo.map(_.addr): _*)
        .subject(mailData.subject)
        .content(Multipart().add(new MimeBodyPart {
          setContent(mailData.html.toString(), "text/html; charset=UTF-8")
        }))
    )
  }
}
