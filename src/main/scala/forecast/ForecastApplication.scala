package forecast

import forecast.model.{TemperatureUnit, WeatherRequest}
import forecast.util.Logging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class ForecastApplication

object ForecastApplication extends  Logging{
  def main(args: Array[String]): Unit = {
    val configuration: Array[Object] = Array(classOf[ForecastApplication])

    val context = SpringApplication.run(configuration, args)

    Option(context.getEnvironment.getProperty("locations")) match {
      case  Some(locations) =>
        locations.split(",").toSet.filter(_.nonEmpty).map(_.trim).toSeq.foreach(loc=>{
          logInfo(s"Starting and checking $loc")
          context.getBean(classOf[WeatherServiceExecutor]).getWeatherResult( WeatherRequest(loc, TemperatureUnit.Celsius))
        })
      case None => ()

    }
  }
}