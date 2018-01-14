package cloudator.model

import java.util.Date

case class WeatherCond(date: Date, temp: Int, description: String, isSevere: Boolean)
