package com.atanana.vk_digest.mailers

import javax.inject.Inject

import com.atanana.vk_digest.{MailConfig, MailData}
import courier.Defaults._
import courier._

import scala.concurrent.Future

class RealMailer @Inject()(private val config: MailConfig) extends Mailer {
  def send(mailData: MailData): Future[Unit] = {
    val mailer = Mailer(config.server, config.port)
      .auth(true)
      .as(config.address, config.password)
      .startTtls(true)()

    val future: Future[Unit] = mailer(
      Envelope.from(config.address.addr)
        .to(config.addressTo.addr)
        .subject(mailData.subject)
        .content(Multipart().html(mailData.html.toString()))
    )
    future.failed.foreach(
      e => println(e.getMessage)
    )
    future
  }
}
