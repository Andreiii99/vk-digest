package com.atanana.vk_digest

import javax.inject.Inject

import courier.Defaults._
import courier._

import scala.concurrent.Future

class Mailer @Inject()(private val config: MailConfig) {
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
