package utils.logging

import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

object Log extends DefaultJsonProtocol {

  implicit val logFormat: RootJsonFormat[Log] = jsonFormat3(Log)

  def logMessage(action: String, details: String, responseCode: Int): String =
    Log(action = action, details = Some(details), responseCode = Some(responseCode)).toJson.toString()

  def logMessage(action: String, details: String): String =
    Log(action = action, details = Some(details)).toJson.toString()

  def logMessage(action: String, responseCode: Int): String =
    Log(action = action, responseCode = Some(responseCode)).toJson.toString()

  sealed case class Log(
    action: String,
    details: Option[String] = None,
    responseCode: Option[Int] = None
  )

}
