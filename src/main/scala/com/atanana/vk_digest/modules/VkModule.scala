package com.atanana.vk_digest.modules

import com.atanana.vk_digest.VkConfig
import com.google.inject.{AbstractModule, Provides}
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import net.codingwell.scalaguice.ScalaModule

class VkModule(vkConfig: VkConfig) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {}

  @Provides
  def vkClient(): VkApiClient = {
    val transportClient = HttpTransportClient.getInstance()
    new VkApiClient(transportClient)
  }

  @Provides
  def vkActor(): UserActor = {
    new UserActor(vkConfig.userId, vkConfig.token)
  }
}
