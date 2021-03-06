package de.leanovate.swaggercheck.schema.model

/**
 * Result of a verification.
 */
sealed trait ValidationResult {
  /**
   * `true` if successful
   */
  def isSuccess: Boolean

  /**
   * Combine this result with another.
   *
   * Only successful if both are successful.
   *
   * @return combined result
   */
  def combine(result: ValidationResult): ValidationResult
}

object ValidationResult {
  /**
   * Create a verification success.
   */
  val success: ValidationResult = ValidationSuccess

  /**
   * Create a verification error.
   *
   * @param failure error message
   */
  def error(failure: String): ValidationResult = ValidationFailure(Seq(failure))
}

case object ValidationSuccess extends ValidationResult {
  override def isSuccess: Boolean = true

  override def combine(result: ValidationResult): ValidationResult = result
}

case class ValidationFailure(failures: Seq[String]) extends ValidationResult {
  override def isSuccess: Boolean = false

  override def combine(result: ValidationResult): ValidationResult = result match {
    case ValidationSuccess => this
    case ValidationFailure(otherFailures) => ValidationFailure(failures ++ otherFailures)
  }
}
