package app

import javax.inject.Singleton
import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._

import scala.collection.mutable

@Singleton
class ApplicationConfig extends AppConfig {
  lazy val config: Config = ConfigFactory.load()
  lazy val appName: String = config.getString("app.name")
  lazy val httpHost: String = config.getString("http.host")
  lazy val httpPort: Int = config.getInt("http.port")

  lazy val mongoDatabase: String = config.getString("mongodb.database")
  lazy val mongoServers: mutable.Buffer[String] = config.getStringList("mongodb.servers").asScala
  lazy val mongoUserName: String = config.getString("mongodb.user")
  lazy val mongoPassword: String = config.getString("mongodb.password")
  lazy val mongoAuthEnabled: Boolean = config.getBoolean("mongodb.authEnabled")
}
