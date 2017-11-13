package com.atanana.vk_digest.vk

import javax.inject.Inject

import com.atanana.vk_digest.VkConfig
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.objects.messages.Message

import scala.collection.JavaConverters._

class MessageProvider @Inject()(
                                 private val actor: UserActor,
                                 private val vkApiClient: VkApiClient,
                                 private val config: VkConfig
                               ) {
  def messages(lastMessageId: Option[Int]): List[Message] = {
    val query = getQuery
    lastMessageId
      .map(id => query.startMessageId(id))
      .getOrElse(query.count(50))
      .execute()
      .getItems
      .asScala.toList
  }

  private def getQuery = {
    vkApiClient.messages()
      .getHistory(actor)
      .peerId(config.chatId)
      .userId(config.userId)
  }
}
