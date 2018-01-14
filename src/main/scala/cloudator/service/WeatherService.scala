package cloudator.service

import cloudator.model.{RequestContext, WeatherRequest, WeatherResult}

import scala.util.Try



trait WeatherService {

  def fetchLocationCond(req:WeatherRequest, ctx:RequestContext): Try[WeatherResult]


}
