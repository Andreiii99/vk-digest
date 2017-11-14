package com.atanana.vk_digest.modules

import com.atanana.vk_digest.{Data, JsonStore}
import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule

class DataModule(jsonStore: JsonStore) extends AbstractModule with ScalaModule {
  private val dataOption: Option[Data] = jsonStore.read

  override def configure(): Unit = {}

  @Provides
  def data(): Option[Data] = {
    dataOption
  }
}
