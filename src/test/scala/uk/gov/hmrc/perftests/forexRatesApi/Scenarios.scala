package uk.gov.hmrc.perftests

import io.gatling.core.Predef.scenario
import uk.gov.hmrc.perftests.forexRatesApi.ForexRequests._
import uk.gov.hmrc.perftests.forexRatesApi.ScenarioDefinition

package object Scenarios {

  def singleDateJourney(smokeTest: Boolean): ScenarioDefinition = {
    val load = 1
    val single = scenario("Retrieve Forex Rate for Single Date")
      .exec(
        triggerRssFeed,
        getForexRateSingleDate
      )
    ScenarioDefinition(single, load)
  }

  def dateRangeJourney(smokeTest: Boolean): ScenarioDefinition = {
    val load = 1
    val dateRange = scenario("Retrieve Forex Rates for Date Range")
      .exec(
        triggerRssFeed,
        getForexRatesDateRange
      )
    ScenarioDefinition(dateRange, load)
  }
}
