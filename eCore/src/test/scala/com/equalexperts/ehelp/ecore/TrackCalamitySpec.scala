package com.equalexperts.ehelp.ecore

import java.util.{Calendar, Date}

import akka.actor.ActorSystem
import akka.event.{LogSource, Logging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString
import clairvoyance.specs2.{ClairvoyantContext, ClairvoyantSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class TrackCalamitySpec extends ClairvoyantSpec {

  "The eCore" should {
    "track a /calamity when someone asks for help using our services" in new context {
      givenSomebodyAsksForHelp(dueTo("flood"), at("Santana neighborhood in São Paulo"), requesting("escaping"))

      whenIAskToSeeAllTheCalamitiesHappening(now)

      thenICanSeeThatSomebodyAskedForHelp(toEscapeFrom("flood"), at("Santana neighborhood in São Paulo"), requesting("escaping"))

    }.pendingUntilFixed("working in progress")
  }
}

trait context extends ClairvoyantContext {

  implicit val system = ActorSystem("eCore")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  // About logging => http://doc.akka.io/docs/akka/snapshot/scala/logging.html
  implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
    def genString(o: AnyRef): String = "spec"
    override def getClazz(o: AnyRef): Class[_] = o.getClass
  }
  val log = Logging(system, this)

  val server = new Server()
  server.startAndWait()

  def givenSomebodyAsksForHelp(calamity: Calamity, location: Location, provision: Provision) {
    httpPost("http://localhost:9000/calamity", jsonWith(calamity, location, provision))
  }

  def whenIAskToSeeAllTheCalamitiesHappening(when: Date) {
    httpGet("http://localhost:9000/hello")
  }

  def thenICanSeeThatSomebodyAskedForHelp(calamity: Calamity, location: Location, provision: Provision) {
  }

  def dueTo(calamity: String): Calamity = Calamity(calamity)

  def at(location: String): Location = {
    location match {
      case "Santana neighborhood in São Paulo" => Location("-23.499648", "-46.628493", location);
    }
  }

  def requesting(helpWith: String): Provision = Provision(helpWith)

  def toEscapeFrom(calamity: String): Calamity = Calamity(calamity)

  def now : Date = Calendar.getInstance().getTime

  override def tearDown(): Unit = server.stop()

  private def httpGet(path: String) : Unit = {
    val eventualResponse: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = path))
    val response: HttpResponse = Await.result(eventualResponse, 1 second)

    response match {
      case HttpResponse(OK, headers, entity, _) => log.debug(s"got response body: \'${unmarshal(response)}\' ")
      case HttpResponse(code, _, _, _)          => log.debug(s"Request failed, response code: ${code}")
    }
  }

  private def httpPost(path: String, json: String): Unit = {
    val postRequest = HttpRequest(POST, path, entity = HttpEntity(MediaTypes.`application/json`, ByteString(json)))
    val eventualResponse: Future[HttpResponse] = Http().singleRequest(postRequest)

    val response: HttpResponse = Await.result(eventualResponse, 1 second)
    response.status match {
      case OK => log.debug("POST request succeeded")
      case _  => log.debug(s"POST request failed with status code \'${response.status}\' and body: \'${unmarshal(response)}\'")
    }
  }

  private def unmarshal(response: HttpResponse): String = {
    val eventualString: Future[String] = Unmarshal(response.entity).to[String]
    Await.result(eventualString, 1 micro)
  }

  private def jsonWith(calamity: Calamity, location: Location, provision: Provision): String = {
    s"""
       |{
       |  "calamity": {
       |    "problem": "${calamity.problem}"
       |  },
       |  "location": {
       |    "latitude": "${location.latitude}",
       |    "longitude": "${location.longitude}",
       |    "description": "${location.description}"
       |  },
       |  "provision": {
       |    "needed": "${provision.needed}"
       |  }
       |}
    """.stripMargin
  }

}
