package com.atanana.vk_digest.modules

import com.atanana.vk_digest._
import com.atanana.vk_digest.mailers.{Mailer, RealMailer}
import com.atanana.vk_digest.ui.UiComposer
import com.atanana.vk_digest.vk.MessageProvider
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class ConfigModule(config: Config) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[VkConfig].toInstance(config.vkConfig)
    bind[MailConfig].toInstance(config.mailConfig)

    bind[Mailer].to[RealMailer]
    bind[MessageProvider]
    bind[UiComposer]
    bind[MessagesProcessor]
  }
}
