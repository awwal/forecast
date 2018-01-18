package cloudator.test

import cloudator.model._
import cloudator.service.{WeatherService, YWJsonParser}
import org.scalatest._

import scala.io.Source
import scala.util.{Failure, Try}


class EvaluationTest extends FlatSpec {


  it should "Parse yahoo weather json correctly " in {

    val testjson = Source.fromResource("sample-yahoo-weather.json").mkString
    val context = RequestContext(-1, 10)


    val alertFunc: Seq[ForecastCond] => Option[Alert] = (_)=>None
    val p: Try[WeatherResult] = YWJsonParser.parse(alertFunc,context, Celsius,"helsinki", testjson)
    assert(p.isSuccess)
    assert(p.get.alert.isEmpty)

  }

  it should "Have alert created" in {

    case object StubService extends WeatherService {
      override def fetchLocationCond(req: WeatherRequest, ctx: RequestContext) = Failure(new NotImplementedError(""))

    }

    val testjson = Source.fromResource("sample-yahoo-weather.json").mkString
    val context = RequestContext(-1, 10)


    val alertFunc: Seq[ForecastCond] => Option[Alert] = (fs)=> StubService.checkForTemperatureAlert(context)(fs)
    val p: Try[WeatherResult] = YWJsonParser.parse(alertFunc,context, Celsius,"helsinki", testjson)
    assert(p.isSuccess)
    println(p.get)
    assert(p.get.alert.nonEmpty)
    println(p.get.alert.get)

    println(Range.inclusive(0,30).intersect(Range(-7,0)))


  }
}
