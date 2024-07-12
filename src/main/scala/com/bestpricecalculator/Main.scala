package com.bestpricecalculator

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import providers.{DataProvider, MockDataProvider}
import models.{Rate, CabinPrice, BestGroupPrice, CabinPriceWithRateGroup}
import scala.util.Try

object Main extends App {
  val dataProvider: DataProvider =
    new MockDataProvider // probably dependency injected in production code

  val futureRates: Future[Seq[Rate]] = dataProvider.fetchRates
  val futureCabinPrices: Future[Seq[CabinPrice]] =
    dataProvider.fetchCabinPrices

  val result =
    for
      rates <- futureRates
      cabinPrices <- futureCabinPrices
    yield (rates, cabinPrices)

  result.onComplete {
    case Success(rates, cabinPrices) => {
      Try(PriceCalculator.getBestGroupPrices(rates, cabinPrices)) match {
        case Success(returnedValues) => {
          println("Best Group Prices Per Cabin:")
          returnedValues.foreach(result => println(result))
        }
        case Failure(ex) => {
          println("Failed to get best group prices")
          ex.printStackTrace
        }
      }
    }
    case Failure(e) => {
      println("Failed to get data from third party provider")
      e.printStackTrace
    }
  }

  Thread.sleep(
    4000
  ) // only required in this case to allow futures to finish execution

}

object PriceCalculator {
  def getBestGroupPrices(
      rates: Seq[Rate],
      prices: Seq[CabinPrice]
  ): Seq[BestGroupPrice] = {
    val rateLookup: Map[String, String] =
      rates.map(rate => rate.rateCode -> rate.rateGroup).toMap

    // Example output showed prices for each combination of cabinCode and group
    val pricesWithGroup: Map[(String, String), Seq[CabinPriceWithRateGroup]] =
      prices
        .flatMap { cabinPrice =>
          rateLookup
            .get(cabinPrice.rateCode)
            .map(rateGroup =>
              CabinPriceWithRateGroup(
                cabinPrice.cabinCode,
                cabinPrice.rateCode,
                cabinPrice.price,
                rateGroup
              )
            )
        }
        .groupBy(price => (price.rateGroup, price.cabinCode))

    // Get cheapest option for each cabin in each rate group
    pricesWithGroup.values
      .flatMap(group => {
        val minPrice = group.minBy(_.price)
        Seq(
          BestGroupPrice(
            minPrice.cabinCode,
            minPrice.rateCode,
            minPrice.price,
            minPrice.rateGroup
          )
        )
      })
      .toSeq
      .sortBy(_.cabinCode) // to match example data, not needed
  }
}
