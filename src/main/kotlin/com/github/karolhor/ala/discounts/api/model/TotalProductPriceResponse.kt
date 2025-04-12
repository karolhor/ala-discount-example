package com.github.karolhor.ala.discounts.api.model

import java.math.BigDecimal
import java.util.UUID

data class TotalProductPriceResponse(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: Price,
    val totalPrice: Price,
    val discountAmount: Price,
    val discountRate: BigDecimal,
    val finalPrice: Price
)
