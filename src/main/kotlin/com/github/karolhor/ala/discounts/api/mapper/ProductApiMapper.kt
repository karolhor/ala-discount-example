package com.github.karolhor.ala.discounts.api.mapper

import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.api.model.TotalProductPriceResponse
import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.domain.model.TotalPrice
import org.springframework.stereotype.Component

@Component
class ProductApiMapper {
    fun productToResponse(product: Product): ProductResponse =
        ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price.toPriceString()
        )

    fun totalPriceToResponse(product: Product, quantity: Int, totalPrice: TotalPrice) = TotalProductPriceResponse(
        productId = product.id,
        quantity = quantity,
        unitPrice = product.price.toPriceString(),
        discount = TotalProductPriceResponse.DiscountValues(
            amount = totalPrice.discountAmount.toPriceString(),
            rate = totalPrice.discountRate.toPriceString(),
        ),
        totalPrice = TotalProductPriceResponse.PriceValues(
            base = totalPrice.totalPrice.toPriceString(),
            final = totalPrice.finalPrice.toPriceString()
        ),
    )
}
