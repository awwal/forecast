package cloudator.test

import cloudator.model.{Celsius, Location, RequestContext, WeatherResult}
import cloudator.service.YWJsonParser
import org.scalatest._

import scala.io.Source
import scala.util.Try


class EvaluationTest extends FlatSpec {


  it should "Parse yahoo weather json correctly " in {

    val testjson = Source.fromResource("sample-yahoo-weather.json").mkString
    val p: Try[WeatherResult] = YWJsonParser.parse(RequestContext(-1, 10), Celsius,Location(1.0,1.0), testjson)
    assert(p.isSuccess)

  }

  it should "Contain 3 locations" in {

    val locs = Location.fromString("-1,-1,0,0,1,1")
    assert(locs.size==3,s"Expecting ${locs.size}")
    assert(locs.exists(loc=>loc.lat.equals(-1d)))
    assert(locs.exists(loc=>loc.lat.equals(0d)))
    assert(locs.exists(loc=>loc.lat.equals(1d)))
  }

  it should "Fail" in {
    assertThrows[Exception]{
      Location.fromString("-1,x")
      fail("This should fail x is not a number")
    }
  }

}
