package cloudator.model


object TemperatureUnit extends Enumeration {
  type TemperatureUnit = Value
  val Fahrenheit, Celsius = Value

  def fromString(sym: String): Option[TemperatureUnit] = sym match {

    case "c" | "C" => Some(Celsius)
    case "f" | "F" => Some(Fahrenheit)
    case _ => None

  }

  val DEGREE = "\u00b0"

  //  @JsonValue
  def getSymbol(tempSymbol: TemperatureUnit): String = tempSymbol match {
    case Fahrenheit => DEGREE + "F"
    case Celsius => DEGREE + "C"
  }
}
