/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.forexRatesApi

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.forexRatesApi.TestUtils.getLastWeekdayAfter4pm

object ForexRequests extends ServicesConfiguration {

  val baseUrl: String           = baseUrlFor("forex-rates")
  val getRatesUrl: String       = "/forex-rates/rates"
  val triggerRssFeedUrl: String = "/forex-rates/test-only/retrieve-rates"
  val dateFrom: String          = getLastWeekdayAfter4pm.minusDays(5).toString
  val dateTo: String            = getLastWeekdayAfter4pm.toString
  val baseCurrency: String      = "EUR"
  val targetCurrency: String    = "GBP"

  def triggerRssFeed: ChainBuilder =
    exec(
      http("Trigger RSS Feed")
        .get(s"$baseUrl$triggerRssFeedUrl")
        .check(status.is(200))
    )

  def getForexRateSingleDate: ChainBuilder =
    exec(
      http("GET Forex Rate for a single date")
        .get(s"$baseUrl$getRatesUrl/$dateTo/$baseCurrency/$targetCurrency")
        .check(status.is(200))
    )

  def getForexRatesDateRange: ChainBuilder =
    exec(
      http("GET Forex Rates for a date range")
        .get(s"$baseUrl$getRatesUrl/$dateFrom/$dateTo/$baseCurrency/$targetCurrency")
        .check(status.is(200))
    )

}
