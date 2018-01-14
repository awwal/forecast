package cloudator.model

import java.text.SimpleDateFormat


case class Location (lat:Double,lon:Double)
case class WeatherRequest(location: Location,tempSymbol: TemperatureUnit=Celsius)

case class RequestContext(minTemp: Int, maxTemp: Int)

case class WeatherResult(currCond: WeatherCond, futureCond: List[WeatherCond], tempSymbol: TemperatureUnit){
  override def toString = {
    import cloudator.service.StringOps._
    val sd = new SimpleDateFormat("EEE,dd MMM yyyy")
    val symbol = tempSymbol.toString
    val predictions = futureCond.sortBy(_.date).take(5)
    val dates: List[String] = predictions.map(cond => sd.format(cond.date))
    val columnLen = 15
    val headers = ("Current".fitToLen(columnLen) :: dates).mkString(" | ")
    val predictTemp = predictions.map(cond => cond.temp + symbol.fitToLen(columnLen-2))
    val body = (currCond.temp + symbol.fitToLen(columnLen-2) :: predictTemp).mkString(" | ")

    val sb = new StringBuffer()
    val NEWLINE = "\n"
    sb.append(NEWLINE)
    sb.append("-"*columnLen*7).append(NEWLINE)
    sb.append(headers).append(NEWLINE)
    sb.append(body).append(NEWLINE)
    sb.append("-"*columnLen*7).append(NEWLINE)
    sb.toString
  }
}
