import cats.data.EitherT
import errors.AppErrors

import scala.concurrent.Future

package object services {
  type EitherF[A, B] = EitherT[Future, A, B]
  type Result[T] = EitherF[AppErrors, T]
}
