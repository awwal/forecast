package cloudator

import cloudator.model.RequestContext
import cloudator.service.YahooWeatherService
import cloudator.util.Logging
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.env.Environment
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

import scala.util.Try

@Configuration
class BeanConfiguration extends  Logging {

  @Autowired private[cloudator] val env :Environment= null

  @Bean
  def threadPoolTaskScheduler: ThreadPoolTaskScheduler = {
    logInfo("Initializing thread")
    val threadPoolTaskScheduler = new ThreadPoolTaskScheduler
    threadPoolTaskScheduler.setPoolSize(1)
    threadPoolTaskScheduler.setThreadNamePrefix("WeatherServiceScheduler")
    threadPoolTaskScheduler
  }

  @Bean def weatherService = new YahooWeatherService(restTemplate)

  @Bean
  def weatherServiceExecutor(): WeatherServiceExecutor = {
    val minTemp =   Try( env.getProperty("mintemp").toInt).getOrElse(-30)
    val maxTemp =   Try( env.getProperty("maxtemp").toInt).getOrElse(+40)
    require(minTemp<maxTemp, "The min temperature should be lesser than max temperature")
    new WeatherServiceExecutor(weatherService,  RequestContext(minTemp, maxTemp))
  }

  @Bean
  private[cloudator] def restTemplate = {
    val restTemplateBuilder = new RestTemplateBuilder
    restTemplateBuilder.setConnectTimeout(500).setReadTimeout(500).build
  }

  @Bean
  def mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter = {
    val converter = new MappingJackson2HttpMessageConverter
    val objectMapper = new ObjectMapper
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    converter.setObjectMapper(objectMapper)
    objectMapper.registerModule(new DefaultScalaModule)
    converter
  }
}