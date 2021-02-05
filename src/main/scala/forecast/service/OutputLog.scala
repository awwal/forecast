package forecast.service

import forecast.model.WeatherRequest
import org.slf4j.LoggerFactory

object OutputLog {
  val loggerName = "outputlog"
  private val LOG = LoggerFactory.getLogger(loggerName)

  def logError(req: WeatherRequest, e: Throwable) = LOG.info(s"Failed to get forecast for ${req.location}")
  def log(string: String):Unit = LOG.info(string)


}
