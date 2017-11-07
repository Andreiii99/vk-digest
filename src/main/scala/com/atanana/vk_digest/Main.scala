package com.atanana.vk_digest

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient

object Main {
  def main(args: Array[String]): Unit = {
    val transportClient = HttpTransportClient.getInstance()
    val vk = new VkApiClient(transportClient)
    val userId = sys.env("userId").toInt
    val actor = new UserActor(userId, sys.env("token"))
    val messages = vk.messages()
      .getHistory(actor)
      .peerId(sys.env("chatId").toInt)
      .userId(userId)
      .count(100)
      .execute()
      .getItems

    messages.forEach(message => {
      println(message.getBody)
    })
  }
}
