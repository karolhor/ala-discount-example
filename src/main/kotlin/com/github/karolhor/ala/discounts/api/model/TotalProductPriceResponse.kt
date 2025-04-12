package com.github.karolhor.ala.discounts.api.model

import java.math.BigDecimal
import java.util.UUID

data class TotalProductPriceResponse(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: String,
    val discount: DiscountValues,
    val price: PriceValues
) {
    data class DiscountValues(
        val amount: String,
        val rate: BigDecimal,
    )

    data class PriceValues(
        val total: String,
        val final: String
    )
}
