package com.equalexperts.ehelp.ecore

import java.util.{Calendar, Date}

import akka.actor.ActorSystem
import akka.event.{LogSource, Logging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString
import clairvoyance.specs2.ClairvoyantSpec
import com.equalexperts.ehelp.ecore.support.ECoreSpecsContext

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions
//import scala.language.postfixOps There's a ambiguity when importing postfixOps

class TrackCalamitySpec extends ClairvoyantSpec {

  "The eCore" should {
    "track a /calamity when someone asks for help using our services" in new context {
      givenSomebodyAsksForHelp(dueTo("flood"), at("Santana neighborhood in São Paulo"), requesting("escaping"))

      whenIAskToSeeAllTheCalamitiesHappening(now)

      thenICanSeeThatSomebodyAskedForHelp(toEscapeFrom("flood"), at("Santana neighborhood in São Paulo"), requesting("escaping"))
    }
  }
}

trait context extends ECoreSpecsContext with Protocols {

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

  var calamitiesResponse: CalamitiesResponseModel = _

  def givenSomebodyAsksForHelp(calamity: Calamity, location: Location, provision: Provision) {
    httpPost("http://localhost:9000/ecore/calamity", jsonWith(calamity, location, provision))
  }

  def whenIAskToSeeAllTheCalamitiesHappening(when: Date) {
    httpGet("http://localhost:9000/ecore/calamities")
  }

  def thenICanSeeThatSomebodyAskedForHelp(expectedCalamity: Calamity, expectedLocation: Location, expectedProvision: Provision) {
    val situations: List[Situation] = calamitiesResponse.situations
    situations must have size 1

    val theSituation: Situation = situations.head
    // Note: Another option is to use Specs2 matching case classes support. More about it here => https://etorreborre.github.io/specs2/guide/SPECS2-3.8.4/org.specs2.guide.Matchers.html#optional
    theSituation.calamity must beEqualTo(expectedCalamity)
    theSituation.location must beEqualTo(expectedLocation)
    theSituation.provision must beEqualTo(expectedProvision)
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

  override def tearDown() = server.stop()

  private def httpGet(uri: String) = {
    captureValue("Request from Client to eHelp" -> uri)

    val eventualResponse: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = uri))
    val response: HttpResponse = Await.result(eventualResponse, 1 second)

    response match {
      case HttpResponse(OK, headers, entity, _) =>
        log.debug(s"got response body: \'${unmarshal(response)}\' ")
        calamitiesResponse = unmarshalToModel(response)
        captureValue("Response from eHelp to Client" -> response)

      case HttpResponse(status, _, _, _) => log.debug(s"Request failed, response code: $status and body: ${unmarshal(response)}")
    }
  }

  private def httpPost(path: String, json: String) {
    val postRequest = HttpRequest(POST, path, entity = HttpEntity(MediaTypes.`application/json`, ByteString(json)))
    val eventualResponse: Future[HttpResponse] = Http().singleRequest(postRequest)

    val response: HttpResponse = Await.result(eventualResponse, 1 second)
    response.status match {
      case OK => log.debug("POST request succeeded")
      case _  => log.debug(s"POST request failed with status code \'${response.status}\' and body: \'${unmarshal(response)}\'")
    }
  }

  private def unmarshal(response: HttpResponse): String = {
    val eventualResponse: Future[String] = Unmarshal(response.entity).to[String]
    Await.result(eventualResponse, 1 micro)
  }

  private def unmarshalToModel(response: HttpResponse): CalamitiesResponseModel = {
    val eventualResponse = Unmarshal(response.entity).to[CalamitiesResponseModel]
    Await.result(eventualResponse, 1 micro)
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

