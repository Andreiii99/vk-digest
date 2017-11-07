package com.atanana.vk_digest

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class JsonStoreTest extends WordSpecLike with BeforeAndAfter with MockFactory with Matchers {
  var fsWrapper: FsWrapper = _
  var jsonStore: JsonStore = _

  before {
    fsWrapper = mock[FsWrapper]
    jsonStore = new JsonStore(fsWrapper)
  }

  "JsonStore" should {
    "write correct data" in {
      val data = Data(123)
      val json =
        """{
          |  "lastMessage": 123
          |}""".stripMargin
      (fsWrapper.writeFile _).expects(json, JsonStore.FILE_NAME)
      jsonStore.write(data)
    }

    "read data" in {
      (fsWrapper.readFile _).expects(JsonStore.FILE_NAME).returns(Success(
        """{
          |  "lastMessage": 123
          |}""".stripMargin))
      jsonStore.read shouldEqual Some(Data(123))
    }

    "return empty data when no file" in {
      (fsWrapper.readFile _).expects(JsonStore.FILE_NAME).returns(Failure(new RuntimeException))
      jsonStore.read shouldEqual None
    }

    "return empty data when invalid json in file" in {
      (fsWrapper.readFile _).expects(JsonStore.FILE_NAME).returns(Success(""))
      jsonStore.read shouldEqual None
    }
  }
}

