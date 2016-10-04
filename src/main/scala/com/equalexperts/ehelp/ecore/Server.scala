package com.equalexperts.ehelp.ecore

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._ // Needed for unmarshalling
import akka.http.scaladsl.server.Directives._
import akka.pattern.{AskableActorRef, ask} // needed for actor asks (?)
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.{implicitConversions, postfixOps}

trait Protocols extends DefaultJsonProtocol {

  implicit val calamityRequestFormat = jsonFormat1(Calamity)
  implicit val locationRequestFormat = jsonFormat3(Location)
  implicit val provisionRequestFormat = jsonFormat1(Provision)
  implicit val situationRequestFormat = jsonFormat3(SituationRequest)

  implicit val situationFormat = jsonFormat3(Situation)
  implicit val calamitiesResponseFormat = jsonFormat1(CalamitiesResponseModel)
}

class Server() extends Protocols {

  implicit val system = ActorSystem("eCore")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher // needed for the future flatMap/onComplete in the end

  implicit val timeout: Timeout = Timeout(1 seconds) // needed for actor asks (?)

  var bindingFuture: Future[ServerBinding] = _

  val calamityTracker: AskableActorRef = system.actorOf(Props[CalamityTrackerActor], "calamityTracker")

  val route =
    logRequestResult("eCore") {
      pathPrefix("ecore") {
        path("calamities") {
          get {
            val calamities = List(Situation(Calamity("flood"), Location("-23.499648", "-46.628493", "Santana neighborhood in São Paulo"), Provision("escaping")))
            complete {
              CalamitiesResponseModel(calamities)
            }
          }
        } ~
          path("calamity") {
            (post & entity(as[SituationRequest])) { situationRequest =>
              complete {
                (calamityTracker ? situationRequest).mapTo[String]
              }
            }
          }
      }
    }

  def start(): Unit = {
    val config = ConfigFactory.load()

    this.bindingFuture = Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))
  }

  def startAndWait(): Unit = {
    val config = ConfigFactory.load()

    this.bindingFuture = Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))
    Await.result(bindingFuture, 5000 millis)
  }

  def stop(): Unit = {
    if (bindingFuture == null) return

    // see http://doc.akka.io/docs/akka/2.4/scala/http/routing-dsl/index.html - Minimal example
    this.bindingFuture
      .flatMap(_.unbind) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
