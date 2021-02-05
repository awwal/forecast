package forecast

import forecast.util.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import scala.util.Try

@Component
class WeatherServiceUpdater extends Logging {
  @Autowired private[forecast] val taskScheduler: TaskScheduler = null
  @Autowired private[forecast] val task: WeatherServiceExecutor = null
  @Autowired private[forecast] val env: Environment = null

  @PostConstruct
  def init(): Unit = {
    val schedulingInterval = Try(env.getProperty("interval.sec").toInt).getOrElse(120)
    logInfo("Scheduling  every " + schedulingInterval + " sec")
    taskScheduler.scheduleWithFixedDelay(task, schedulingInterval * 1000)
  }
}