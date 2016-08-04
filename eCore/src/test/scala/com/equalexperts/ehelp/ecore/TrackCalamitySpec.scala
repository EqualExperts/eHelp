package com.equalexperts.ehelp.ecore

import java.util.{Calendar, Date}

import clairvoyance.specs2.{ClairvoyantContext, ClairvoyantSpec};

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

  def givenSomebodyAsksForHelp(value: Any, value1: Any, value2: Any): Unit = {

  }

  def whenIAskToSeeAllTheCalamitiesHappening(now: Date): Unit = {

  }

  def thenICanSeeThatSomebodyAskedForHelp(value: Any, value1: Any, value2: Any) = {

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

}
