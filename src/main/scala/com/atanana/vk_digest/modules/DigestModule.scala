package com.atanana.vk_digest.modules

import com.atanana.vk_digest.{ConfigProvider, FsWrapper}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class DigestModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[FsWrapper]
    bind[ConfigProvider]
  }
}
