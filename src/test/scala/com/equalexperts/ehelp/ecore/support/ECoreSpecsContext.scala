package com.equalexperts.ehelp.ecore.support

import java.io.File._

import clairvoyance.ProducesCapturedInputsAndOutputs
import clairvoyance.plugins.SequenceDiagram
import clairvoyance.specs2.ClairvoyantContext
import org.specs2.matcher._

import scala.util.Properties

trait ECoreSpecsContext extends ClairvoyantContext with EnablesSupportForSpecs2Matchers with SequenceDiagram with ProducesCapturedInputsAndOutputs {

  Properties.setProp("specs2.outDir", s"docs$separator");

  override def capturedInputsAndOutputs = Seq(this)
}

trait EnablesSupportForSpecs2Matchers extends Matchers with MustExpectations with MustThrownExpectations with ShouldExpectations with ShouldThrownExpectations

