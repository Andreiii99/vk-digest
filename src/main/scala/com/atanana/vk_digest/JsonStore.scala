package com.atanana.vk_digest

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.Try

class JsonStore(fsWrapper: FsWrapper) {

  import JsonStore.FILE_NAME

  private implicit val dataFormat: RootJsonFormat[Data] = jsonFormat1(Data)

  def read: Option[Data] = {
    fsWrapper.readFile(FILE_NAME)
      .flatMap(contents => Try {
        contents.parseJson.convertTo[Data]
      })
      .toOption
  }

  def write(data: Data): Unit = {
    fsWrapper.writeFile(data.toJson.prettyPrint, FILE_NAME)
  }
}

case class Data(lastMessage: Int)

object JsonStore {
  val FILE_NAME = "data.json"

  def apply(fsWrapper: FsWrapper): JsonStore = new JsonStore(fsWrapper)
}