package com.atanana.vk_digest.mailers

import javax.inject.Inject

import com.atanana.vk_digest.{FsWrapper, MailData}

import scala.concurrent.Future

class TestMailer @Inject()(private val fsWrapper: FsWrapper) extends Mailer {
  override def send(mailData: MailData): Future[Unit] = {
    fsWrapper.writeFile(mailData.html.toString(), s"${mailData.subject}_${System.nanoTime()}.html")
    Future.successful()
  }
}
