package com.atanana.vk_digest.ui

import com.vk.api.sdk.objects.messages.Message
import com.vk.api.sdk.objects.users.User
import play.twirl.api.Html

class TwirlWrapper {
  def main(title: String, body: Html): Html = {
    html.main(title, body)
  }

  def messages(messages: List[Message], users: Map[Int, User], messagesConstructor: (List[Message]) => Html): Html = {
    html.messages(messages, users, messagesConstructor)
  }
}
