package com.atanana.vk_digest

import java.io.FileNotFoundException

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class ConfigProviderTest extends WordSpecLike with BeforeAndAfter with MockFactory with Matchers {
  var fsWrapper: FsWrapper = _
  var provider: ConfigProvider = _

  before {
    fsWrapper = stub[FsWrapper]
    provider = new ConfigProvider(fsWrapper)
  }

  "ConfigProvider" should {
    "read config" in {
      (fsWrapper.readFile _).when(ConfigProvider.FILE_NAME).returns(Success(
        """{
          |  "vkConfig": {
          |    "userId": 123,
          |    "token": "test token",
          |    "chatId": 321
          |  },
          |  "mailConfig": {
          |    "server": "test server",
          |    "port": 456,
          |    "address": "test@test.com",
          |    "password": "test password",
          |    "addressTo": [
          |        "test_to@test.com",
          |        "test_to2@test.com"
          |    ],
          |    "subjectPrefix": "Дайджест"
          |  }
          |}""".stripMargin
      ))
      provider.config() shouldEqual Success(Config(
        VkConfig(123, "test token", 321),
        MailConfig("test server", 456, "test@test.com", "test password", List("test_to@test.com", "test_to2@test.com"), "Дайджест")
      ))
    }

    "not fails on reading config" in {
      (fsWrapper.readFile _).when(ConfigProvider.FILE_NAME).returns(Failure(new FileNotFoundException()))
      provider.config() shouldBe a[Failure[_]]
    }
  }
}
