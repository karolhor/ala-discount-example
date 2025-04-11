package com.github.karolhor.ala.discounts.api.mapper

import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.domain.model.Product
import org.springframework.stereotype.Component

@Component
class ProductApiMapper {
    fun productToResponse(product: Product): ProductResponse =
        ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            priceInCents = product.price,
            price = formatPrice(product.price)
        )

    private fun formatPrice(priceInCents: Int): String {
        val price = priceInCents.toDouble() / CENT_FACTOR
        return price.to2DecimalString()
    }

    private fun Double.to2DecimalString(): String = String.format("%.2f", this)

    companion object {
        private const val CENT_FACTOR = 100
    }
}
