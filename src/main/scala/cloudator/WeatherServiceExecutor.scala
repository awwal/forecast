package cloudator

import java.util.concurrent.{Callable, ExecutorService, Executors}

import cloudator.model.{RequestContext, WeatherRequest, WeatherResult}
import cloudator.service.{OutputLog, WeatherService}
import cloudator.util.Logging
import com.google.common.cache.{CacheBuilder, CacheLoader}
import com.google.common.util.concurrent.{ListenableFuture, ListenableFutureTask}

import scala.collection.JavaConverters._
import scala.collection.immutable.Iterable
import scala.util.{Failure, Success, Try}

class WeatherServiceExecutor(weatherService: WeatherService, requestContext: RequestContext) extends  Runnable with Logging {



 private val loadingCache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .build(new WeatherResultCacheLoader)


  override def run() = {
    val keys = loadingCache.asMap.keySet
    logInfo("Refreshing cache. Size "+keys.size)
    keys.asScala.foreach(key=>loadingCache.refresh(key))

    loadingCache.asMap().values().asScala.foreach(wr=>OutputLog.log(wr.toString))
  }

  def getWeatherResults(requests:Seq[WeatherRequest]):Seq[WeatherResult] ={
      requests.flatMap(req=>getWeatherResult(req))
  }


  def getWeatherResult(request:WeatherRequest):Option[WeatherResult] ={
    logDebug("Attempt to get result for location "+request.location)
    Try(loadingCache.get(request)).toOption
  }

  def fetchAllAvailableWeatherResult():Seq[WeatherResult] ={
    val keys = loadingCache.asMap.keySet
    logDebug("Fetching all "+keys.size()+" result available")
    keys.asScala.flatMap(key=> getWeatherResult(key)).toSeq
  }


  class WeatherResultCacheLoader extends CacheLoader[WeatherRequest, WeatherResult] with Logging {


    val executor: ExecutorService = Executors.newFixedThreadPool(1)

    override
    def load(req: WeatherRequest): WeatherResult = {

      weatherService.fetchLocationCond(req, requestContext) match {
        case Success(wr) =>
          wr
        case Failure(e) =>
          OutputLog.logError(req,e)
          throw e
      }
    }

    override
    def reload(key: WeatherRequest, oldValue: WeatherResult): ListenableFuture[WeatherResult] = {


      val task: ListenableFutureTask[WeatherResult] = ListenableFutureTask.create(new Callable[WeatherResult]() {
        override def call: WeatherResult = {
          load(key)
        }
      })
      executor.execute(task)
      task

    }
  }

}
