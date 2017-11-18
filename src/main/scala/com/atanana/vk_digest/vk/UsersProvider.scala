package com.atanana.vk_digest.vk

import javax.inject.Inject

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.objects.users.User

import scala.collection.JavaConverters._

class UsersProvider @Inject()(private val actor: UserActor, private val vkApiClient: VkApiClient) {
  def users(ids: List[Int]): Map[Int, User] = {
    vkApiClient.users()
      .get(actor)
      .userIds(ids.map(_.toString).asJava)
      .execute()
      .asScala
      .map(user => user.getId.toInt -> user)
      .toMap
  }
}
