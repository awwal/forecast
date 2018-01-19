package cloudator

import java.text.SimpleDateFormat
import java.util.Locale

import cloudator.model.WeatherResult
import cloudator.service.JsonUtil
import com.vaadin.annotations.Theme
import com.vaadin.data.Property
import com.vaadin.server._
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui._
import com.vaadin.ui.themes.ValoTheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.client.RestTemplate

import scala.util.{Success, Try}
import com.vaadin.ui.VerticalLayout

@Theme("valo")
@SpringUI
@SerialVersionUID(-1L)
class ApplicationUI extends UI {

  @Autowired
  val env: Environment = null


  override protected
  def init(request: VaadinRequest): Unit = {
    val forecastService: ForecastService = new ForecastService(env)
    VaadinSession.getCurrent.setLocale(Locale.UK)
    Responsive.makeResponsive(this)

    val tableLayout = new HorizontalLayout()
    tableLayout.setSpacing(true)

    val textField = new TextField()
    val addBtn = new Button("Add")
    val refreshBtn: Button = new Button("Refresh")

    refreshBtn.setIcon(FontAwesome.REFRESH)
    refreshBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY)

    val addLayout = new HorizontalLayout(new Label("City"), textField, addBtn)
    addLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT)
    addLayout.setSpacing(true)



    def addTotableLayout(wr: WeatherResult): Unit = {
      tableLayout.addComponent(ForecastTable.createComponent(wr))
    }


    addBtn.addClickListener(ev => {

      if (textField.getValue == null) return
      val city = textField.getValue
      forecastService.submit(city).foreach(wr => {
        addTotableLayout(wr)
      })
    })


    refreshBtn.addClickListener(ev => {
      tableLayout.removeAllComponents()
      forecastService.fetchAll.foreach(wr => {
        addTotableLayout(wr)

      })

    })

    val layout: VerticalLayout = new VerticalLayout(addLayout, refreshBtn, tableLayout)
    layout.setSpacing(true)
    layout.setMargin(true)
    setContent(layout)

  }

}


object ForecastTable {

  def createComponent(wr: WeatherResult): Component = {

    import wr._

    val table = new Table()
    val datePPty = "Date"
    val tempPPty = "Temperature"
    val dateFormat = new SimpleDateFormat("EEE,dd MMM yyyy")


    val symbol = tempSymbol.toString
    val predictions = futureCond.sortBy(_.date).take(5)
    table.addContainerProperty(datePPty, classOf[String], "")
    table.addContainerProperty(tempPPty, classOf[String], "")

    val item = table.addItem(wr.currCond)
    item.getItemProperty(datePPty).asInstanceOf[Property[String]].setValue(dateFormat.format(wr.currCond.date))
    item.getItemProperty(tempPPty).asInstanceOf[Property[String]].setValue(s"${wr.currCond.temp}")

    predictions.foreach(fc => {
      val item = table.addItem(fc)
      item.getItemProperty(datePPty).asInstanceOf[Property[String]].setValue(dateFormat.format(fc.date))
      item.getItemProperty(tempPPty).asInstanceOf[Property[String]].setValue(s"${fc.min}Low ${fc.max}High")
    })

    table.setPageLength(6)
    table.setCaption(wr.location)

    val layout = new VerticalLayout(table)
    wr.alert match{
      case Some(al) =>
        val label = new Label("Alert" + al.description)
        label.setWidth("260px")
        label.addStyleName(ValoTheme.LABEL_FAILURE)
        layout.addComponent(label)
      case _ =>

    }
    layout.setSpacing(true)
    layout

  }


}


class ForecastService(env: Environment) {
  def submit(city: String): Seq[WeatherResult] = {
    val url = "http://localhost:" + Option(env.getProperty("server.port")).getOrElse("8080")
    val restTemplate = new RestTemplate()
    val response = Try(restTemplate.getForObject(url + s"/api/forecasts/?locations=$city", classOf[String]))
    toDomainObjects(response)
  }


  private def toDomainObjects(response: Try[String]) = {
    response match {
      case Success(json) =>
        val ls = JsonUtil.fromJson[Seq[WeatherResult]](json)
        ls.sortBy(_.location)
      case _ => List()
    }
  }

  def fetchAll: Seq[WeatherResult] = {
    val url = "http://localhost:" + Option(env.getProperty("server.port")).getOrElse("8080")
    val restTemplate = new RestTemplate()
    val response = Try(restTemplate.getForObject(url + "/api/forecasts", classOf[String]))
    toDomainObjects(response)
  }

}