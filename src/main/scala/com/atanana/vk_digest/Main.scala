package com.atanana.vk_digest

import com.atanana.vk_digest.modules.{ConfigModule, DigestModule, VkModule}
import com.google.inject.{Guice, Injector}
import net.codingwell.scalaguice.InjectorExtensions._

import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    val rootInjector: Injector = Guice.createInjector(new DigestModule)
    rootInjector.instance[ConfigProvider].config() match {
      case Success(config) =>
        val injector = rootInjector.createChildInjector(
          new ConfigModule(config),
          new VkModule(config.vkConfig)
        )
      case Failure(e) => println(e.getMessage)
    }
  }
}
