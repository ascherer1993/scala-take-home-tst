package com.bestpricecalculator.providers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import com.bestpricecalculator.models.{Rate, CabinPrice}

trait DataProvider {
  def fetchRates: Future[Seq[Rate]]
  def fetchCabinPrices: Future[Seq[CabinPrice]]
}
