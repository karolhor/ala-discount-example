package com.github.karolhor.ala.discounts.domain

import com.github.karolhor.ala.discounts.domain.model.Product
import java.util.UUID

interface ProductRepository {
    suspend fun findProductById(productId: UUID): Product?
}
