package forecast.test

import forecast.model.{TemperatureUnit, WeatherResult}
import forecast.service.JsonUtil
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.{content, status}


@RunWith(classOf[SpringRunner])
@SpringBootTest
@AutoConfigureMockMvc
class APITest {
  @Autowired
  private val mockMvc:MockMvc = null

  @Test
  @throws[Exception]
  def testForecastFetchAllAPICallIsOk(): Unit = {

    mockMvc.perform(get("/api/forecasts").accept(MediaType.APPLICATION_JSON))
      .andDo(print).andExpect(content.contentType("application/json;charset=UTF-8"))
      .andExpect(status.isOk).andReturn
  }


  @Test
  @throws[Exception]
  def testForecastFetchForALocation(): Unit = {

    val result =mockMvc.perform(get("/api/forecasts?locations=helsinki&unit=f").accept(MediaType
      .APPLICATION_JSON))
      .andDo(print).andExpect(content.contentType("application/json;charset=UTF-8"))
      .andExpect(status.isOk).andReturn

    val jsonContent = result.getResponse.getContentAsString
    assert(jsonContent!=null)

    val ls: Seq[WeatherResult] = JsonUtil.fromJson[Seq[WeatherResult]](jsonContent)

    assertEquals("Expected only one result since only one location was provided",1,ls.size)
    assertEquals(ls.head.tempSymbol, TemperatureUnit.Fahrenheit)

  }


  @Test
  @throws[Exception]
  def testMultiplelocations(): Unit = {

    val result =mockMvc.perform(get("/api/forecasts?locations=helsinki,lagos&unit=c").accept(MediaType
      .APPLICATION_JSON))
      .andDo(print).andExpect(content.contentType("application/json;charset=UTF-8"))
      .andExpect(status.isOk).andReturn

    val jsonContent = result.getResponse.getContentAsString
    assert(jsonContent!=null)

    val ls: Seq[WeatherResult] = JsonUtil.fromJson[Seq[WeatherResult]](jsonContent)

    assertEquals("Expected only two result since only one location was provided",2,ls.size)
    assertEquals(ls.head.tempSymbol, TemperatureUnit.Celsius)

  }



}
