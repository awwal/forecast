package forecast.service

import forecast.model._

import java.text.SimpleDateFormat
import scala.util.Try


trait WeatherService {

  def fetchLocationCond(req: WeatherRequest, ctx: RequestContext): Try[WeatherResult]

  def checkForTemperatureAlert(ctx: RequestContext)(cond: Seq[ForecastCond]): Option[Alert] = {
    val dateFormat = new SimpleDateFormat("EEE,dd MMM")

    val safeRange = Range.inclusive(ctx.minTemp, ctx.maxTemp)

    def withinRange(fc: ForecastCond) = safeRange.contains(fc.min) && safeRange.contains(fc.max)

    cond.sortBy(_.date).find(fc => !withinRange(fc)) match {
      case Some(fc) =>
        Some(Alert(s"Forecast Temperature [${fc.min} ${fc.max}] on ${dateFormat.format(fc.date)} " +
          s"overshoots tolerable range [${ctx.minTemp} ${ctx.maxTemp}]"))
      case _ => None
    }

  }

}
