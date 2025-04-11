package com.github.karolhor.ala.discounts.repository.db.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.UUID

@Table(name = "products")
data class ProductEntity(
    @Id
    val id: UUID,

    val name: String,
    val description: String,
    val price: BigDecimal
)
