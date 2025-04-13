package com.github.karolhor.ala.discounts.domain.model

import java.math.BigDecimal

data class TotalPrice(
    val discountRate: BigDecimal,
    val totalPrice: BigDecimal,
    val discountAmount: BigDecimal,
    val finalPrice: BigDecimal,
)
