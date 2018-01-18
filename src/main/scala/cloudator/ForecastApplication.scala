package cloudator

import cloudator.model.{Celsius, WeatherRequest}
import cloudator.util.Logging
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
        locations.split(",").map(_.trim).foreach(loc=>{
          logInfo(s"Starting and checking $loc")
          context.getBean(classOf[WeatherServiceExecutor]).getWeatherResult( WeatherRequest(loc, Celsius))
        })
      case None => ()

    }
  }
}