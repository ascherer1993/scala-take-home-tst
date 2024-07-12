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
  println(allCombinablePromotions(allPromotions))

  combinablePromotions("P3", allPromotions).foreach(println(_))

  sleep(4000)
}

def allCombinablePromotions(
    allPromotions: Seq[Promotion]
): Seq[PromotionCombo] = {

  def isValidSubset(subset: Set[Promotion]): Boolean = {
    subset.forall(p1 =>
      subset.forall(p2 => p1 == p2 || !p1.notCombinableWith.contains(p2.code))
    )
  }

  def allSubsets[T](seq: Seq[T]): Seq[Seq[T]] = {
    seq match {
      case Nil => Seq(Seq.empty)
      case head +: tail =>
        val tailSubsets = allSubsets(tail)
        tailSubsets ++ tailSubsets.map(head +: _)
    }
  }
  val validSubsets = allPromotions.toSet.subsets.filter(isValidSubset).map(_.toSeq).toSeq
  //println(validSubsets)

  def isMaximalSubset(subset: Seq[Promotion]): Boolean = {
    !validSubsets.exists(other =>
      other.size > subset.size && subset.forall(other.contains)
    )
  }

  // Find all maximal valid subsets
  val maximalSubsets = validSubsets.filter(isMaximalSubset)

  maximalSubsets.map(subset => PromotionCombo(subset.map(_.code).sorted))
}

def allCombinablePromotions2(
    allPromotions: Seq[Promotion]
): Seq[PromotionCombo] = {

  // Helper functions
  def isValidSubset(subset: Seq[Promotion]): Boolean = {
    subset.forall(p1 =>
      subset.forall(p2 => p1 == p2 || !p1.notCombinableWith.contains(p2.code))
    )
  }

  def allSubsets[T](seq: Seq[T]): Seq[Seq[T]] = {
    seq match {
      case Nil => Seq(Seq.empty)
      case head +: tail =>
        val tailSubsets = allSubsets(tail)
        tailSubsets ++ tailSubsets.map(head +: _)
    }
  }
  val validSubsets = allSubsets(allPromotions).filter(isValidSubset)

  def isMaximalSubset(subset: Seq[Promotion]): Boolean = {
    !validSubsets.exists(other =>
      other.size > subset.size && subset.forall(other.contains)
    )
  }

  // Find all maximal valid subsets
  val maximalSubsets = validSubsets.filter(isMaximalSubset)

  // Convert to PromotionCombo
  maximalSubsets.map(subset => PromotionCombo(subset.map(_.code)))
}

def combinablePromotions(
    promotionCode: String,
    allPromotions: Seq[Promotion]
): Seq[PromotionCombo] = {
  def allSubsets[T](seq: Seq[T]): Seq[Seq[T]] = {
    seq match {
      case Nil => Seq(Seq.empty)
      case head +: tail =>
        val tailSubsets = allSubsets(tail)
        tailSubsets ++ tailSubsets.map(head +: _)
    }
  }
  def isValidSubset(subset: Seq[Promotion]): Boolean = {
    subset.forall(p1 =>
      subset.forall(p2 => p1 == p2 || !p1.notCombinableWith.contains(p2.code))
    )
  }
  val validSubsetsContaingValue = allSubsets(allPromotions).filter(f => f.map(g => g.code).contains(promotionCode)).filter(isValidSubset)

  def isMaximalSubset(subset: Seq[Promotion]): Boolean = {
    !validSubsetsContaingValue.exists(other =>
      other.size > subset.size && subset.forall(other.contains)
    )
  }

  // Find all maximal valid subsets
  val maximalSubsets = validSubsetsContaingValue.filter(isMaximalSubset)

  // Convert to PromotionCombo
  maximalSubsets.map(subset => PromotionCombo(subset.map(_.code)))
}
