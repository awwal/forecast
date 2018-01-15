package cloudator.service

import cloudator.Logging
import cloudator.model.{Celsius, RequestContext, WeatherRequest, WeatherResult}
import org.springframework.web.client.RestTemplate

import scala.util.Try

class YahooWeatherService(restTemplate: RestTemplate) extends WeatherService with Logging {

  private
  val basePath = "http://query.yahooapis.com/v1/public/yql?format=json&q="

  override def fetchLocationCond(req: WeatherRequest, ctx: RequestContext): Try[WeatherResult] = {
    import req.location._
    val unitFilter: String = if (req.temprUnit.equals(Celsius)) """u='c'""" else """u='f'"""

    val yqlRaw =
      s"""
         |select item from weather.forecast
         | where ${unitFilter}  and woeid in
         | (select woeid from geo.places(1) where text="($lat,$lon)")
      """.stripMargin.replaceAll("\n", "")

    logDebug(s"YQL generated\n $yqlRaw")
    //    val yql = basePath+ URLEncoder.encode(yqlRaw,"utf-8")
    val yql = basePath + yqlRaw
    val response = Try(restTemplate.getForObject(yql, classOf[String]))
    logTrace(response.toString)
    val res = response.flatMap(r => YWJsonParser.parse(ctx, req.temprUnit,req.location, r))
    res

  }

}
