package com.github.karolhor.ala.discounts.api.model

import java.util.UUID

data class TotalProductPriceResponse(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: String,
    val discount: DiscountValues,
    val totalPrice: PriceValues
) {
    data class DiscountValues(
        val amount: String,
        val rate: String,
    )

    data class PriceValues(
        val base: String,
        val final: String
    )
}
