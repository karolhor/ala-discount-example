package com.github.karolhor.ala.discounts.api.model

import java.util.UUID

data class TotalProductPriceResponse(
    val productId: UUID,
    val quantity: Int,
    val totalPrice: Price
)
