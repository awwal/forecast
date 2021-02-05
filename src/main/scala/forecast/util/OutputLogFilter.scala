package forecast.util

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import forecast.service.OutputLog

class OutputLogFilter extends Filter[ILoggingEvent] {
  override def decide(event: ILoggingEvent): FilterReply = {

    event.getLoggerName match {
      case OutputLog.loggerName => FilterReply.ACCEPT
      case _ => FilterReply.DENY
    }
  }
}