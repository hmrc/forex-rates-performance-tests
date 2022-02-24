/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.forexRatesApi

import io.gatling.core.Predef._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import uk.gov.hmrc.performance.conf.PerftestConfiguration
import uk.gov.hmrc.perftests.Scenarios
import scala.concurrent.duration._


class ForexSimulation extends Simulation with PerftestConfiguration {

  private def withInjectedLoad(scenarioDefinitions: Seq[ScenarioDefinition]): Seq[PopulationBuilder] = scenarioDefinitions.map(scenarioDefinition => {
    val load = loadPercentage * scenarioDefinition.load
    println(s"Running scenario: ${scenarioDefinition.builder.name} at ${loadPercentage * 100}% of default ${scenarioDefinition.load} load = $load")

    val injectionSteps = List(
      rampUsersPerSec(noLoad).to(load).during(rampUpTime),
      constantUsersPerSec(load).during(constantRateTime),
      rampUsersPerSec(load).to(noLoad).during(rampDownTime)

    )
    scenarioDefinition.builder.inject(injectionSteps)
  })

  val scenarioDefinitions: Seq[ScenarioDefinition] =
      Seq(
        Scenarios.singleDateJourney(runSingleUserJourney),
        Scenarios.dateRangeJourney(runSingleUserJourney)
      )

  println(s"Setting up simulation")

  if (runSingleUserJourney) {
    println(s"'perftest.runSmokeTest' is set to true, ignoring all loads and running with only one user per journey!")
    val injectedBuilders = scenarioDefinitions.map(scenarioDefinition => {
      scenarioDefinition.builder.inject(atOnceUsers(1))
    })

    setUp(injectedBuilders: _*)
      .assertions(global.failedRequests.count.is(0))
  }
  else {
    setUp(withInjectedLoad(scenarioDefinitions): _*)
      .assertions(global.failedRequests.percent.lte(1)).maxDuration(10 minutes)
  }

  }

  case class ScenarioDefinition(builder: ScenarioBuilder, load: Double) {
    def this(scenarioBuilder: ScenarioBuilder) {
      this(scenarioBuilder, 1.0)
    }
}
