package com.github.karolhor.ala.discounts.domain.model

import java.math.BigDecimal
import java.util.UUID

data class Product(
    val id: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal
)
