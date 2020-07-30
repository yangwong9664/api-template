package errors

sealed trait AppErrors extends Throwable

final case class SystemError(message: Option[String] = None) extends AppErrors
