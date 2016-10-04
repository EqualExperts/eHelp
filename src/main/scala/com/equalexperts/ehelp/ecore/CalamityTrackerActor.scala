package com.equalexperts.ehelp.ecore

import akka.actor.Actor
import akka.event.Logging

class CalamityTrackerActor extends Actor {

  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SituationRequest(calamity, location, provision) => sender ! s"Received $calamity"
    case _ => log.info("received unknown message")
  }
}
