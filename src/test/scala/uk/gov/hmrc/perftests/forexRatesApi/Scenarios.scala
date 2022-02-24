/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
