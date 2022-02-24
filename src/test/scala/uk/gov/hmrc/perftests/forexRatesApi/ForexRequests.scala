/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.forexRatesApi

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import org.joda.time.LocalDate
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object ForexRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("forex-rates")
  val getRatesUrl: String   = "/rates"
  val triggerRssFeedUrl: String   = "/test-only/retrieve-rates"
  val dateFrom = LocalDate.now().minusDays(5).toString()
  val dateTo = LocalDate.now().toString()
  val baseCurrency = "EUR"
  val targetCurrency = "GBP"


  def triggerRssFeed: ChainBuilder =
    exec(http("Trigger RSS Feed")
      .get(s"$baseUrl$triggerRssFeedUrl")
      .check(status.is(200)))

  def getForexRateSingleDate: ChainBuilder =
    exec(http("GET Forex Rate for a single date")
      .get(s"$baseUrl$getRatesUrl/$dateTo/$baseCurrency/$targetCurrency")
      .check(status.is(200)))

  def getForexRatesDateRange: ChainBuilder =
    exec(http("GET Forex Rates for a date range")
      .get(s"$baseUrl$getRatesUrl/$dateFrom/$dateTo/$baseCurrency/$targetCurrency")
      .check(status.is(200)))

}
