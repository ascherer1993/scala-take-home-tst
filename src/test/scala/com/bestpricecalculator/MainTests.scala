package com.bestpricecalculator.tests

import com.bestpricecalculator.Main
import com.bestpricecalculator.models.Rate
import com.bestpricecalculator.models.CabinPrice
// Tests
// General case
// Empty list
// NotCorrectCasing?
// Mismatch on the rate codes
// Duplicates
//
class MainTests extends munit.FunSuite {
  test("example test that succeeds") {
    val rateSeq = Seq(Rate("M1","Testing"))
    var response = Main.getBestGroupPrices(rateSeq, Seq(CabinPrice("C1", "M1", 300)))
    assertEquals(response.length, 1)
  }
}