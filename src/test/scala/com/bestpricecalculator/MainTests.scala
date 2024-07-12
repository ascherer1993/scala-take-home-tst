package com.bestpricecalculator.tests

import com.bestpricecalculator.PriceCalculator.getBestGroupPrices
import com.bestpricecalculator.models.Rate
import com.bestpricecalculator.models.CabinPrice
import com.bestpricecalculator.models.CabinPriceWithRateGroup
import com.bestpricecalculator.models.BestGroupPrice

class MainTests extends munit.FunSuite {

  //Test data
  val validExampleRates = Seq(
    Rate("M1", "Military"),
    Rate("M2", "Military"),
    Rate("S1", "Senior"),
    Rate("S2", "Senior")
  )
  val validExampleRates2 = Seq(
    Rate("M3", "Military"),
    Rate("M4", "Military"),
    Rate("S3", "Senior"),
    Rate("S4", "Senior")
  )
  val validExampleCabinPrices = Seq(
    CabinPrice("CA", "M1", 200.00),
    CabinPrice("CA", "M2", 250.00),
    CabinPrice("CA", "S1", 225.00),
    CabinPrice("CA", "S2", 260.00),
    CabinPrice("CB", "M1", 230.00),
    CabinPrice("CB", "M2", 260.00),
    CabinPrice("CB", "S1", 245.00),
    CabinPrice("CB", "S2", 270.00)
  )
  val validExampleCabinPrices2 = Seq(
    CabinPrice("CA", "M3", 200.00),
    CabinPrice("CA", "M4", 250.00),
    CabinPrice("CA", "S3", 225.00),
    CabinPrice("CA", "S4", 260.00),
    CabinPrice("CB", "M3", 230.00),
    CabinPrice("CB", "M4", 260.00),
    CabinPrice("CB", "S3", 245.00),
    CabinPrice("CB", "S4", 270.00)
  )

  test("getBestGroupPrices should return cheapest cabin rategroup combo for example data") {

    val expected = Seq(
      BestGroupPrice("CA", "M1", 200, "Military"),
      BestGroupPrice("CA", "S1", 225, "Senior"),
      BestGroupPrice("CB", "M1", 230, "Military"),
      BestGroupPrice("CB", "S1", 245, "Senior")
    )

    var response =
      getBestGroupPrices(validExampleRates, validExampleCabinPrices)
    assertEquals(response, expected)
  }

  test("getBestGroupPrices should handle rates without matching prices") {
    val expected = Seq.empty[BestGroupPrice]
    val result = getBestGroupPrices(validExampleRates, validExampleCabinPrices2)
    assertEquals(result, expected)
  }

  test("getBestGroupPrices should return no results for no rates") {
    val emptyRatesSeq = Seq.empty[Rate]
    var response = getBestGroupPrices(emptyRatesSeq, validExampleCabinPrices)
    assertEquals(response.length, 0)
  }

  test("getBestGroupPrices should return no results for no cabin prices") {
    val emptyCabinPricesSeq = Seq.empty[CabinPrice]
    var response = getBestGroupPrices(validExampleRates, emptyCabinPricesSeq)
    assertEquals(response.length, 0)
  }
  
  //
}
