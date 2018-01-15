package cloudator

import java.util.{Collection => JCol}

import cloudator.model._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters._


@RestController
class WeatherController {


  @Autowired
  var weatherServiceExecutor: WeatherServiceExecutor = _


  @GetMapping(value = Array("/api/forecasts"), produces = Array(APPLICATION_JSON_UTF8_VALUE))
  @ResponseBody
  def getAll(@RequestParam(name = "location", required = false) location: String,
             @RequestParam(name = "unit", required = false) tempUnit: String): JCol[WeatherResult] = {

    if (location == null)
      weatherServiceExecutor.fetchAllAvailableWeatherResult().asJavaCollection
    else {

      val tempSymbol = if ("f".equalsIgnoreCase(tempUnit)) Fahrenheit else Celsius
      val requests = Location.fromString(location).map(loc => WeatherRequest(loc, tempSymbol))

      weatherServiceExecutor.getWeatherResults(requests).asJava
    }
  }


}
