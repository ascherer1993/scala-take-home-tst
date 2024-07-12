package com.allcombinablepromotions.providers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import com.allcombinablepromotions.models.Promotion

class MockDataProvider extends DataProvider {
  override def fetchPromotionalCodes: Future[Seq[Promotion]] =
    Future {
      Thread.sleep(800)
      Seq(
        Promotion("P1", Seq("P3")),
        Promotion("P2", Seq("P4", "P5")),
        Promotion("P3", Seq("P1")),
        Promotion("P4", Seq("P2")),
        Promotion("P5", Seq("P2"))
      )
    }
}
