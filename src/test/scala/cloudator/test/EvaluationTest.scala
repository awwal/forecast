package cloudator.test

import cloudator.model.{Celsius, RequestContext, WeatherResult}
import cloudator.service.YWJsonParser
import org.scalatest._

import scala.io.Source
import scala.util.Try


class EvaluationTest extends FlatSpec {


  it should "Parser yahoo weather " in {

    val testjson = Source.fromResource("sample-yahoo-weather.json").mkString
    val p: Try[WeatherResult] = YWJsonParser.parse(RequestContext(-1, 10), Celsius, testjson)
    println(p.get.toString)

  }
}
