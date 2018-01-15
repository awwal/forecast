package cloudator.service

import java.util.Date

import cloudator.model._
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape._

import scala.beans.BeanProperty
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


  def parse(ctx:RequestContext, temprUnit: TemperatureUnit, location: Location,json: String): Try[WeatherResult] = {
    val qr = JsonUtil.fromJson[QResult](json)
    val item = qr.query.results.channel.item


    val cond = item.condition
    val forecast = item.forecast
    val currCond = WeatherCond(cond.date, cond.temp, cond.text)
    val forecastList = forecast.map(fc => ForecastCond(fc.date, fc.low,fc.high, fc.text))

    val wr = WeatherResult(System.currentTimeMillis(),temprUnit,location, currCond,forecastList, alert = None)
    Success(wr)
  }


}
