package cloudator.model

import java.text.SimpleDateFormat
import java.util.Date


case class WeatherRequest(location: String, temprUnit: TemperatureUnit = Celsius)

case class RequestContext(minTemp: Int, maxTemp: Int)

case class Alert(description: String) extends AnyVal

case class WeatherResult(updateTime: Long, tempSymbol: TemperatureUnit, location: String,
                         currCond: WeatherCond, futureCond: List[ForecastCond], alert: Option[Alert]) {

  override def toString: String = WeatherResult.format(this)
}




object WeatherResult {
  val dateFormat = new SimpleDateFormat("EEE,dd MMM yyyy")
  val timeFormat = new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:SS")
  val NEWLINE = "\n"


  def format(wr: WeatherResult): String = {
    import cloudator.util.StringOps._
    import wr._
    val symbol = tempSymbol.toString
    val predictions = futureCond.sortBy(_.date).take(5)
    val dates: List[String] = predictions.map(cond => dateFormat.format(cond.date))
    val columnLen = 15
    val headers = ("Current".fitToLen(columnLen) :: dates).mkString(" | ")

    def tempRange(min: String, max: String): String = {
      val tr = min + "Low  " + max + "High "
      val s = tr.fitToLen(columnLen)
      s
    }

    val predictTemp = predictions.map(cond => tempRange(cond.min + "", cond.max + ""))
    val alertDescription = wr.alert.map(_.description).getOrElse("None")
    val body = (s"${currCond.temp}$symbol".fitToLen(columnLen) :: predictTemp).mkString(" | ")

    val timeUpdated = timeFormat.format(new Date(updateTime))
    val unitUsed = if (tempSymbol == Celsius) "Celsius" else "Fahrenheit"

    val sb = new StringBuffer()
    sb.append(NEWLINE)
    sb.append("-" * columnLen * 7).append(NEWLINE)
    sb.append(s"LOC: ${wr.location}  UPDATED: $timeUpdated UNIT: $unitUsed ").append(NEWLINE)
    sb.append("-" * columnLen * 7).append(NEWLINE)
    sb.append(headers).append(NEWLINE)
    sb.append(body).append(NEWLINE)
    sb.append("-" * columnLen * 7).append(NEWLINE)
    sb.append("ALERT: "+alertDescription).append(NEWLINE)
    sb.append("-" * columnLen * 7).append(NEWLINE)
    sb.toString
  }

}