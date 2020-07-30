package app

import com.typesafe.config.Config

import scala.collection.mutable

trait AppConfig {
  val config: Config
  val appName: String
  val httpHost: String
  val httpPort: Int

  val mongoDatabase: String
  val mongoServers: mutable.Buffer[String]
  val mongoUserName: String
  val mongoPassword: String
  val mongoAuthEnabled: Boolean
}
