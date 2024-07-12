package com.bestpricecalculator.models

case class CabinPriceWithRateGroup(
    cabinCode: String,
    rateCode: String,
    price: BigDecimal,
    rateGroup: String
)
