package utils

import app.AppConfig
import models.{Test, TestDB}

trait ModelFormatter extends AppSystem {

  trait Formatter {
    def bsonFormat(value: Test)(implicit appConfig: AppConfig): TestDB
  }

  implicit class BSONGroupsFormat(value: Test) {
    def toBsonModel(implicit formatter: Formatter,appConfig: AppConfig):
    TestDB = formatter.bsonFormat(value)(appConfig)
  }

  implicit val formatter = new Formatter {

    def bsonFormat(yang: Test)(implicit appConfig: AppConfig): TestDB = {
      TestDB(yang.test)
    }
  }

}
