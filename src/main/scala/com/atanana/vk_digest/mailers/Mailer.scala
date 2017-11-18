package com.atanana.vk_digest.mailers

import com.atanana.vk_digest.ui.MailData

import scala.concurrent.Future

trait Mailer {
  def send(mailData: MailData): Future[Unit]
}
