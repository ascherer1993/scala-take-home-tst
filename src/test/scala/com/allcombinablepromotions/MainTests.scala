package com.allcombinablepromotions.tests

import com.allcombinablepromotions.PromotionUtil
import com.allcombinablepromotions.models.Promotion
import com.allcombinablepromotions.models.PromotionCombo
import com.allcombinablepromotions.PromotionUtil.combinablePromotions
import com.allcombinablepromotions.PromotionUtil.allCombinablePromotions

class PromotionUtilTests extends munit.FunSuite {
  // IsMaximalSubset
  test("isMaximalSubset should return true for a maximal subset") {
    val allSubsets = Seq(
      Seq(Promotion("P1", Seq("P3")), Promotion("P2", Seq("P3"))),
      Seq(
        Promotion("P1", Seq("P3")),
        Promotion("P2", Seq("P3")),
        Promotion("P4", Seq.empty)
      )
    )
    val subset = Seq(
      Promotion("P1", Seq("P3")),
      Promotion("P2", Seq("P3")),
      Promotion("P4", Seq.empty)
    )

    assert(PromotionUtil.isMaximalSubset(allSubsets, subset))
  }

  test("isMaximalSubset should return false for a non-maximal subset") {
    val allSubsets = Seq(
      Seq(Promotion("P1", Seq("P3")), Promotion("P2", Seq("P3"))),
      Seq(
        Promotion("P1", Seq("P3")),
        Promotion("P2", Seq("P3")),
        Promotion("P4", Seq.empty)
      )
    )
    val subset = Seq(Promotion("P1", Seq("P3")), Promotion("P2", Seq("P3")))
    assert(!PromotionUtil.isMaximalSubset(allSubsets, subset))
  }

  // IsValidSubset
  test("isValidSubset should return true for a valid subset") {
    val promotions = Seq(
      Promotion("P1", Seq("P4")),
      Promotion("P2", Seq("P5")),
      Promotion("P3", Seq.empty)
    )
    assert(PromotionUtil.isValidSubset(promotions))
  }

  test("isValidSubset should return false for an invalid subset") {
    val promotions = Seq(
      Promotion("P1", Seq("P2")),
      Promotion("P2", Seq("P1"))
    )
    assert(!PromotionUtil.isValidSubset(promotions))
  }

  val allPromotions = Seq(
    Promotion("P1", Seq("P3")),
    Promotion("P2", Seq("P4", "P5")),
    Promotion("P3", Seq("P1")),
    Promotion("P4", Seq("P2")),
    Promotion("P5", Seq("P2"))
  )

  // combinablePromotions
  test("combinablePromotions should return expected PromotionCombos for P1") {

    val promotionCode = "P1"
    val expected = Seq(
      PromotionCombo(Seq("P1", "P2")),
      PromotionCombo(Seq("P1", "P4", "P5"))
    )
    assertEquals(combinablePromotions(promotionCode, allPromotions), expected)
  }

  test("combinablePromotions should return expected PromotionCombos for P3") {

    val promotionCode = "P3"
    val expected = Seq(
      PromotionCombo(Seq("P3", "P2")),
      PromotionCombo(Seq("P3", "P4", "P5"))
    )
    assertEquals(combinablePromotions(promotionCode, allPromotions), expected)
  }

  // allCombinablePromotions
  test("allCombinablePromotions should return expected PromotionCombos") {
    val expected = Seq(
      PromotionCombo(Seq("P1", "P2")),
      PromotionCombo(Seq("P1", "P4", "P5")),
      PromotionCombo(Seq("P2", "P3")),
      PromotionCombo(Seq("P3", "P4", "P5"))
    )
    assertEquals(allCombinablePromotions(allPromotions), expected)
  }
}
