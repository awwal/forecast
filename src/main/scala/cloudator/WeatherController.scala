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
  def getAll(@RequestParam(name = "locations", required = false) locations: String,
             @RequestParam(name = "unit", required = false) tempUnit: String): JCol[WeatherResult] = {

    if (locations == null)
      weatherServiceExecutor.fetchAllAvailableWeatherResult().asJavaCollection
    else {

      val tempSymbol = if ("f".equalsIgnoreCase(tempUnit)) Fahrenheit else Celsius
      val requests =locations.split(",").map(loc => WeatherRequest(loc.trim, tempSymbol))

      weatherServiceExecutor.getWeatherResults(requests).asJava
    }
  }


}
