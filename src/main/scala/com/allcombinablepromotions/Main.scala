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
  def isValidSubset(subset: Seq[Promotion]): Boolean = {
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

  // Get all combinable promotions for provided code
  def combinablePromotions(
      promotionCode: String,
      allPromotions: Seq[Promotion]
  ): Seq[PromotionCombo] = {
    val promotion: Option[Promotion] =
      allPromotions.find(_.code == promotionCode)

    promotion match {
      case Some(desiredPromotion) => {
        val promotionsMinusDesiredCode: Seq[Promotion] =
          allPromotions.filterNot(_ == desiredPromotion)

        val validCombinablePromotionsWithoutDesiredPromotion
            : Seq[Seq[Promotion]] = allValidCombinablePromotions(
          promotionsMinusDesiredCode
        )

        val combinedPromotions: Seq[Seq[Promotion]] =
          validCombinablePromotionsWithoutDesiredPromotion
            .map(combo => desiredPromotion +: combo)
            .filter(isValidSubset)

        val maximalSubsets: Seq[Seq[Promotion]] = combinedPromotions.filter(
          isMaximalSubset(combinedPromotions, _)
        )

        maximalSubsets
          .map(subset => PromotionCombo(subset.map(_.code)))
      }
      case None => {
        println("Failed to find promotion")
        Seq.empty
      }
    }
  }

  // Get all combinable promotions
  def allCombinablePromotions(
      allPromotions: Seq[Promotion]
  ): Seq[PromotionCombo] = {
    val validCombinablePromotions: Seq[Seq[Promotion]] =
      allValidCombinablePromotions(allPromotions);

    val maximalSubsets: Seq[Seq[Promotion]] = validCombinablePromotions.filter(
      isMaximalSubset(validCombinablePromotions, _)
    )

    maximalSubsets
      .sortBy(_.head.code) // to match example data
      .map(subset => PromotionCombo(subset.map(_.code)))
  }

  // Recursive function that gets all valid promotions
  def allValidCombinablePromotions(
      promotions: Seq[Promotion]
  ): Seq[Seq[Promotion]] = {
    promotions match {
      case Nil         => Seq.empty
      case head :: Nil => Seq(Seq(head))
      case head :: tail =>
        val allValidTailCombinations: Seq[Seq[Promotion]] =
          allValidCombinablePromotions(tail)
        (Seq(head) +: allValidTailCombinations) ++
          allValidTailCombinations
            .map(combo => head +: combo)
            .filter(isValidSubset)
    }
  }
}
