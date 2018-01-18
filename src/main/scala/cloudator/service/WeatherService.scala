package cloudator.service

import cloudator.model._

import scala.util.Try


trait WeatherService {

  def fetchLocationCond(req: WeatherRequest, ctx: RequestContext): Try[WeatherResult]

  def checkForTemperatureAlert(ctx: RequestContext)(cond: Seq[ForecastCond]): Option[Alert] = {
    val safeRange = Range.inclusive(ctx.minTemp, ctx.maxTemp)

    def tempRange(forecastCond: ForecastCond) = Range.inclusive(forecastCond.min, forecastCond.max)

    cond.sortBy(_.date).find(fc => safeRange.intersect(tempRange(fc)).isEmpty) match {
      case Some(fc) =>
        Some(Alert(s"Forecast Temperature [${fc.min} ${fc.max}] on ${fc.date} " +
              s"overshoots tolerable range [${ctx.minTemp} ${ctx.maxTemp}]"))
      case _ => None
    }

  }

}
