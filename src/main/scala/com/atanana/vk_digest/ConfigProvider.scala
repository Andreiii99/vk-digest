package com.atanana.vk_digest

class ConfigProvider {
  def config(): Config = {
    Config(
      sys.env("userId").toInt,
      sys.env("token"),
      sys.env("chatId").toInt
    )
  }
}

case class Config(userId: Int, token: String, chatId: Int)