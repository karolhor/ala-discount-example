package com.github.karolhor.ala.discounts.api.mapper

import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.domain.model.Product
import org.springframework.stereotype.Component

@Component
class ProductApiMapper(
    private val priceApiMapper: PriceApiMapper
) {
    fun productToResponse(product: Product): ProductResponse =
        ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            price = priceApiMapper.priceToResponse(product.price)
        )
}
