package cloudator.model

sealed trait TemperatureUnit {
  override def toString: String = TemperatureUnit.toSymbol(this)
}

case object Fahrenheit extends TemperatureUnit

case object Celsius extends TemperatureUnit

object TemperatureUnit {
  def fromString(sym: String): Option[TemperatureUnit] = sym match {

    case "c" | "C" => Some(Celsius)
    case "f" | "F" => Some(Fahrenheit)
    case _ => None

  }

  val DEGREE = "\u00b0"

  def toSymbol(tempSymbol: TemperatureUnit): String = tempSymbol match {
    case Fahrenheit => DEGREE + "F"
    case Celsius => DEGREE + "C"
  }
}
