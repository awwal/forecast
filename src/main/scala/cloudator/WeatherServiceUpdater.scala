package cloudator

import javax.annotation.PostConstruct

import cloudator.util.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component

import scala.util.Try

@Component
class WeatherServiceUpdater extends Logging {
  @Autowired private[cloudator] val taskScheduler: TaskScheduler = null
  @Autowired private[cloudator] val task: WeatherServiceExecutor = null
  @Autowired private[cloudator] val env: Environment = null

  @PostConstruct
  def init(): Unit = {
    val schedulingInterval = Try(env.getProperty("interval.sec").toInt).getOrElse(120)
    logInfo("Scheduling  every " + schedulingInterval + " sec")
    taskScheduler.scheduleWithFixedDelay(task, schedulingInterval * 1000)
  }
}