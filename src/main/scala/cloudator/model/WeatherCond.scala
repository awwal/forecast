package cloudator.model

import java.util.Date

case class WeatherCond(date: Date, temp: Int, description: String)
case class ForecastCond(date: Date, min: Int, max:Int, description: String)
