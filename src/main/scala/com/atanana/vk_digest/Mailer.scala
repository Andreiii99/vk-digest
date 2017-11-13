package com.atanana.vk_digest

import javax.inject.Inject

import courier.Defaults._
import courier._

import scala.concurrent.Future

class Mailer @Inject()(private val config: MailConfig) {
  def send(subject: String, html: String): Future[Unit] = {
    val mailer = Mailer(config.server, config.port)
      .auth(true)
      .as(config.address, config.password)
      .startTtls(true)()

    val future: Future[Unit] = mailer(
      Envelope.from(config.address.addr)
        .to(config.addressTo.addr)
        .subject(subject)
        .content(Multipart().html(html))
    )
    future.failed.foreach(
      e => println(e.getMessage)
    )
    future
  }
}
