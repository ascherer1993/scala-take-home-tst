package com.bestpricecalculator.providers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import com.bestpricecalculator.models.CabinPrice
import com.bestpricecalculator.models.Rate

class MockDataProvider extends DataProvider {
  override def fetchRates: Future[Seq[Rate]] = 
    Future {
      Thread.sleep(800)
      Seq(
        Rate("M1", "Military"),
        Rate("M2", "Military"),
        Rate("S1", "Senior"),
        Rate("S2", "Senior")
      )
    }
  
  override def fetchCabinPrices: Future[Seq[CabinPrice]] = 
    Future {
      Thread.sleep(1000)
      Seq(
        CabinPrice("CA", "M1", 200.00),
        CabinPrice("CA", "M2", 250.00),
        CabinPrice("CA", "S1", 225.00),
        CabinPrice("CA", "S2", 260.00),
        CabinPrice("CB", "M1", 230.00),
        CabinPrice("CB", "M2", 260.00),
        CabinPrice("CB", "S1", 245.00),
        CabinPrice("CB", "S2", 270.00)
      )
    }
  }

