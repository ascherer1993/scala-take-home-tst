package com.allcombinablepromotions.providers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import com.allcombinablepromotions.models.Promotion

trait DataProvider {
  def fetchPromotionalCodes: Future[Seq[Promotion]]
}
