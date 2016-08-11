package com.equalexperts.ehelp.ecore

import java.util.{Calendar, Date}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import clairvoyance.specs2.{ClairvoyantContext, ClairvoyantSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class TrackCalamitySpec extends ClairvoyantSpec {

  "The eCore" should {
    "track a calamity when someone asks for help using our services" in new context {

      givenSomebodyAsksForHelp(dueTo("flood"), at("Santana neighborhood in São Paulo"), requesting("escaping"));

      whenIAskToSeeAllTheCalamitiesHappening(now);

      thenICanSeeThatSomebodyAskedForHelp(toEscapeFrom("flood"), at("Santana neighborhood in São Paulo"), requesting("escaping"));

    }.pendingUntilFixed("working in progress")
  }
}

trait context extends ClairvoyantContext {

  implicit val system = ActorSystem("eCore")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val server = new Server();

  def givenSomebodyAsksForHelp(value: Any, value1: Any, value2: Any): Unit = {
  }

  def whenIAskToSeeAllTheCalamitiesHappening(now: Date): Unit = {

  }

  def thenICanSeeThatSomebodyAskedForHelp(value: Any, value1: Any, value2: Any) = {
    httpGet("http://localhost:9000/hello")
  }

  def httpGet(path: String) : Unit = {
    val futureResponse: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = path))
    val response: HttpResponse = Await.result(futureResponse, 1 second)

    response match {
      case HttpResponse(StatusCodes.OK, headers, entity, _) =>
        println("Got response, body: " + entity.toStrict(1 seconds).map(_.data.decodeString("UTF-8")))
      case HttpResponse(code, _, _, _) =>
        println("Request failed, response code: {}", code)
    };
  }

  def dueTo(calamity: String): Calamity = new Calamity(calamity)

  def at(location: String): Location = {
    location match {
      case "Santana neighborhood in São Paulo" => new Location("-23.499648", "-46.628493");
    }
  };

  def requesting(helpWith: String): Provision = new Provision(helpWith)

  def toEscapeFrom(calamity: String): Calamity = new Calamity("calamity")

  def now : Date = Calendar.getInstance().getTime()

  server.startAndWait();

  override def tearDown(): Unit = {
    server.stop();
  }

}
