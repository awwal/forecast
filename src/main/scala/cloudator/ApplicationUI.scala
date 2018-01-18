package cloudator

import com.vaadin.server._
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.spring.annotation.SpringViewDisplay
import com.vaadin.ui._
import java.util.Locale

import cloudator.model.WeatherResult
import cloudator.service.JsonUtil
import com.vaadin.shared.ui.label.ContentMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.client.RestTemplate

import scala.util.{Success, Try}

@SpringUI
//@SpringViewDisplay
@SerialVersionUID(-1L)
class ApplicationUI extends UI {

  @Autowired
  val env:Environment = null

  val citySelector :NativeSelect= new NativeSelect("Choose city")

  override protected
  def init(request: VaadinRequest): Unit = {
    VaadinSession.getCurrent.setLocale(Locale.UK)
//    Responsive.makeResponsive(this)
    import com.vaadin.ui.VerticalLayout
    citySelector.setNullSelectionAllowed(false)
    citySelector.addItems("Turku", "San Francisco", "London")
    citySelector.addValueChangeListener((e) => {
        // using e.g. ProgressIndicator
//        display.setForecast(service.getForecast(e.getValue))
      ForecastService.fetch(env)

    })
    citySelector.setValue("Turku")

    val layout: VerticalLayout = new VerticalLayout(citySelector)
    setContent(layout)

  }

}


object ForecastService {

  def fetch(env: Environment): Unit ={

    val url ="http://localhost:"+ Option(env.getProperty("server.port")).getOrElse("8080")

    val restTemplate = new RestTemplate()
    val response = Try(restTemplate.getForObject( url+"/api/forecasts", classOf[String]))
    println(response)
    response match {
      case  Success(json) =>
         val ls=  JsonUtil.fromJson[Seq[WeatherResult]](json)
          println(ls)
    }


  }

}