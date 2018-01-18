package cloudator.service

import cloudator.model.WeatherRequest
import org.slf4j.LoggerFactory

object OutputLog {
  val loggerName = "outputlog"
  private val LOG = LoggerFactory.getLogger(loggerName)

  def logError(req: WeatherRequest, e: Throwable) = LOG.info(s"Failed to get forecast with req $req")
  def log(string: String):Unit = LOG.info(string)


}
