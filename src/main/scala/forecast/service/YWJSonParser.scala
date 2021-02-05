package forecast.service

import forecast.model.TemperatureUnit.TemperatureUnit
import forecast.model._
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape._

import java.util.Date
import scala.util.{Success, Try}

//Sat, 13 Jan 2018 12:00 PM AKST
case class Forecast(code: Int,
                    @JsonFormat(shape = STRING, pattern = "dd MMM yyyy") date: Date,
                    high: Int, low: Int,
                    text: String)

case class Condition(code: Int,
                     @JsonFormat(shape = STRING, pattern = "EEE, dd MMM yyyy hh:mm a z") date: Date,
                     temp: Int, text: String)

case class Item(title: String, condition: Condition,
                forecast: List[Forecast])

case class Channel(item: Item)

case class Results(channel: Channel)

case class Query(results: Results)

case class QResult(query: Query)

object YWJsonParser {


  def parse(alertFunc: Seq[ForecastCond] => Option[Alert],ctx: RequestContext,
            temprUnit:TemperatureUnit, location: String, json: String): Try[WeatherResult] = {
    val qr = JsonUtil.fromJson[QResult](json)
    val item = qr.query.results.channel.item


    val cond = item.condition
    val forecast = item.forecast
    val currCond = WeatherCond(cond.date, cond.temp, cond.text)
    val forecastList = forecast.map(fc => ForecastCond(fc.date, fc.low, fc.high, fc.text))

    val alert = alertFunc(forecastList)

    val wr = WeatherResult(System.currentTimeMillis(), temprUnit, location, currCond, forecastList, alert)
    Success(wr)
  }


}
