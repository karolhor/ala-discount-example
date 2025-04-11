package com.github.karolhor.ala.discounts.domain

import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.repository.ProductRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductProvider(
    private val repository: ProductRepository
) {
    suspend fun getProductById(productId: UUID): Product =
        repository.findProductById(productId) ?: throw ProductNotFoundException(productId)
}

class ProductNotFoundException(productId: UUID) : RuntimeException("Product not found. Id: $productId")
