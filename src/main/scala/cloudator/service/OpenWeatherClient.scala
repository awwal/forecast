//package cloudator.service
//
//import javax.ws.rs.client.{Client, ClientBuilder, WebTarget}
//import javax.ws.rs.core.MediaType
//
//import cloudator.Logging
//import cloudator.model.WeatherConfig
//import com.google.gson.GsonBuilder
//import org.glassfish.jersey.client.ClientProperties._
//
//import scala.io.Source
//
//
//class OpenWeatherClient(apiKey: String, weatherConfig: WeatherConfig) extends Logging {
//
//  import weatherConfig._
//
//  private val gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create()
//  private val JSON = MediaType.APPLICATION_JSON_TYPE
//  val baseUrl = "http://api.openweathermap.org/data/2.5"
//  val apiUrl = "forecast?id=524901&APPID=$apiKey"
//  val currentUrl = s"$baseUrl.openweathermap.org/data/2.5/weather?q={city name}"
//
//
//  //  val apiUrl = "https://dev.acada.ng/api/"
//
//  //  val client: Client = JerseyHelper.getClient
//  val client: Client = ClientBuilder.newClient()
//  client.property(CONNECT_TIMEOUT, 1000)
//  client.property(READ_TIMEOUT, 1000)
//  val baseTarget = client.target(baseUrl)
//    .queryParam("APPID", apiKey)
//    .queryParam("units", metricUnit)
//    .queryParam("lat", lat.toString)
//    .queryParam("lon", lon.toString)
//
//
//  def getWeathe(): Unit = {
//
//    val target = baseTarget.path("/weather")
//    printResponse(target)
//  }
//
//
//  def getWeather(): Unit = {
//
//    val target = baseTarget.path("/forecast").queryParam("cnt","3")
//    printResponse(target)
//
//  }
//
//  private def printResponse(target: WebTarget) = {
//    val res = target.request.accept(JSON).get
//
//    val jsonResponse = res.readEntity(classOf[String])
//    println(jsonResponse)
//  }
//
//}
//
//
//object OpenWeatherClient extends Logging {
//
//  def apply(weatherConfig: WeatherConfig): OpenWeatherClient = {
//
//    val apikeyOpt: Option[String] = Source.fromResource("api.key").mkString.split("\n").headOption
//    require(apikeyOpt.nonEmpty, "API key not found")
//    val apiKey = apikeyOpt.get
//
//    new OpenWeatherClient(apiKey, weatherConfig)
//  }
//}