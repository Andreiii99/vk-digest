package com.atanana.vk_digest

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.objects.messages.Message

import scala.collection.JavaConverters._

class MessageProvider(
                       private val actor: UserActor,
                       private val vkApiClient: VkApiClient,
                       private val config: Config
                     ) {
  def messages(lastMessageId: Option[Int]): List[Message] = {
    val query = getQuery
    lastMessageId
      .map(id => query.startMessageId(id))
      .getOrElse(query.count(20))
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
