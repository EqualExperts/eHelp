package com.equalexperts.ehelp.ecore

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Server() {

  implicit val system = ActorSystem("eCore")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher // needed for the future flatMap/onComplete in the end

  var bindingFuture: Future[ServerBinding] = null;

  val route =
    logRequestResult("akka-http-microservice") {
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      }
    }

  def start(): Unit = {
    val config = ConfigFactory.load();

    this.bindingFuture = Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))
  }

  def startAndWait(): Unit = {
    val config = ConfigFactory.load();

    this.bindingFuture = Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))
    Await.result(bindingFuture, 5000 millis);
  }

  def stop(): Unit = {
    // see http://doc.akka.io/docs/akka/2.4/scala/http/routing-dsl/index.html - Minimal example
    this.bindingFuture
      .flatMap(_.unbind) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
