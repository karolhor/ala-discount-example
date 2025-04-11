package com.github.karolhor.ala.discounts.api.model

import java.util.UUID

data class ProductResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val priceInCents: Int,
    val price: String
)
