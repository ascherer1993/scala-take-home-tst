package com.allcombinablepromotions

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import com.allcombinablepromotions.models.{Promotion, PromotionCombo}
import com.allcombinablepromotions.providers.{DataProvider, MockDataProvider}

object Main extends App {
  val dataProvider: DataProvider =
    new MockDataProvider // probably dependency injected in production code. Could be database, could be third party

  val futurePromotions: Future[Seq[Promotion]] =
    dataProvider.fetchPromotionalCodes

  futurePromotions.onComplete {
    case Success(promotions) => {
      println("All Promotion Combos:")
      PromotionUtil.allCombinablePromotions(promotions).foreach(println(_))

      println("\nPromotion Combos for P1:")
      PromotionUtil
        .combinablePromotions("P1", promotions)
        .foreach(println(_))

      println("\nPromotion Combos for P3:")
      PromotionUtil
        .combinablePromotions("P3", promotions)
        .foreach(println(_))
    }
    case Failure(e) => {
      println("Failed to get data from data provider")
      e.printStackTrace
    }
  }

  Thread.sleep(
    4000
  ) // only required in this case to allow futures to finish execution
}

object PromotionUtil {
  // Determines if the provided subset matches the requirement of not having any codes that aren't compatible
  def isValidSubset(subset: Set[Promotion]): Boolean = {
    subset.forall(p1 =>
      subset.forall(p2 => p1 == p2 || !p1.notCombinableWith.contains(p2.code))
    )
  }

  // Determines if the provided subset is the max size subset with regards to the provided seq of subsets
  def isMaximalSubset(
      allSubsets: Seq[Seq[Promotion]],
      subset: Seq[Promotion]
  ): Boolean = {
    !allSubsets.exists(other =>
      other.size > subset.size && subset.forall(other.contains)
    )
  }

  // Gets all combinable promotions for the provided promotionCode
  def combinablePromotions(
      promotionCode: String,
      allPromotions: Seq[Promotion]
  ): Seq[PromotionCombo] = {

    val allSubsetsWithCode = allPromotions.toSet.subsets.filter(f =>
      f.map(g => g.code).contains(promotionCode)
    );

    val validSubsetsContaingCode =
      allSubsetsWithCode.filter(isValidSubset).map(_.toSeq).toSeq

    val maximalSubsets = validSubsetsContaingCode.filter(
      isMaximalSubset(validSubsetsContaingCode, _)
    )

    maximalSubsets.map(subset => PromotionCombo(subset.map(_.code).sorted))
  }

  // Gets all combinable promotions for the provided promotion code
  def allCombinablePromotions(
      allPromotions: Seq[Promotion]
  ): Seq[PromotionCombo] = {

    val validSubsets =
      allPromotions.toSet.subsets.filter(isValidSubset).map(_.toSeq).toSeq

    val maximalSubsets = validSubsets.filter(isMaximalSubset(validSubsets, _))

    maximalSubsets
      .map(subset => PromotionCombo(subset.map(_.code).sorted))
      .sortBy(
        _.promotionCodes(0)
      ) // added sorts to match order of expected output
  }
}
