package com.atanana.vk_digest

import javax.inject.Inject

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.Try

class ConfigProvider @Inject()(private val fsWrapper: FsWrapper) {

  import ConfigProvider.FILE_NAME

  private implicit val vkConfigFormat: RootJsonFormat[VkConfig] = jsonFormat3(VkConfig)
  private implicit val mailConfigFormat: RootJsonFormat[MailConfig] = jsonFormat6(MailConfig)
  private implicit val configFormat: RootJsonFormat[Config] = jsonFormat2(Config)

  def config(): Try[Config] = {
    fsWrapper.readFile(FILE_NAME)
      .flatMap(contents => Try {
        contents.parseJson.convertTo[Config]
      })
  }
}

object ConfigProvider {
  val FILE_NAME = "config.json"
}

case class VkConfig(userId: Int, token: String, chatId: Int)

case class MailConfig(
                       server: String,
                       port: Int,
                       address: String,
                       password: String,
                       addressTo: List[String],
                       subjectPrefix: String
                     )

case class Config(vkConfig: VkConfig, mailConfig: MailConfig)