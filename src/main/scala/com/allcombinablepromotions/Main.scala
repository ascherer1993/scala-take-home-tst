package com.allcobinablepromotions

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import com.allcombinablepromotions.models.Promotion
import com.allcombinablepromotions.models.PromotionCombo

def sleep(time: Long): Unit = Thread.sleep(time)

object Main extends App {
  val allPromotions = Seq(
    Promotion("P1", Seq("P3")),
    Promotion("P2", Seq("P4", "P5")),
    Promotion("P3", Seq("P1")),
    Promotion("P4", Seq("P2")),
    Promotion("P5", Seq("P2"))
  )
  println("All Promotion Combos:")
  allCombinablePromotions(allPromotions).foreach(println(_))

  println("\nPromotion Combos for P1:")
  combinablePromotions("P1", allPromotions).foreach(println(_))

  println("\nPromotion Combos for P3:")
  combinablePromotions("P3", allPromotions).foreach(println(_))
  sleep(4000)
}
def isValidSubset(subset: Set[Promotion]): Boolean = {
    subset.forall(p1 =>
      subset.forall(p2 => p1 == p2 || !p1.notCombinableWith.contains(p2.code))
    )
  }

def isMaximalSubset(allSubsets: Seq[Seq[Promotion]], subset: Seq[Promotion]): Boolean = {
    !allSubsets.exists(other =>
      other.size > subset.size && subset.forall(other.contains)
    )
  }

def combinablePromotions(
    promotionCode: String,
    allPromotions: Seq[Promotion]
): Seq[PromotionCombo] = {

  val allSubsetsWithCode = allPromotions.toSet.subsets.filter(f => f.map(g => g.code).contains(promotionCode));
  val validSubsetsContaingCode = allSubsetsWithCode.filter(isValidSubset).map(_.toSeq).toSeq

  // Find all maximal valid subsets
  val maximalSubsets = validSubsetsContaingCode.filter(isMaximalSubset(validSubsetsContaingCode, _))

  maximalSubsets.map(subset => PromotionCombo(subset.map(_.code)))
}

def allCombinablePromotions(
    allPromotions: Seq[Promotion]
): Seq[PromotionCombo] = {
  
  val validSubsets = allPromotions.toSet.subsets.filter(isValidSubset).map(_.toSeq).toSeq

  // Find all maximal valid subsets
  val maximalSubsets = validSubsets.filter(isMaximalSubset(validSubsets, _))

  maximalSubsets.map(subset => PromotionCombo(subset.map(_.code).sorted)).sortBy(_.promotionCodes(0)) //added sortby to match order of expected output
}