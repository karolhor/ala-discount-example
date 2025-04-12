package com.github.karolhor.ala.discounts.api.mapper

import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.api.model.TotalProductPriceResponse
import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.domain.model.TotalPrice
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

    fun totalPriceToResponse(product: Product, quantity: Int, totalPrice: TotalPrice) = TotalProductPriceResponse(
        productId = product.id,
        quantity = quantity,
        unitPrice = priceApiMapper.priceToResponse(product.price),
        totalPrice = priceApiMapper.priceToResponse(totalPrice.totalPrice),
        discountAmount = priceApiMapper.priceToResponse(totalPrice.discountAmount),
        discountRate = totalPrice.discountRate,
        finalPrice = priceApiMapper.priceToResponse(totalPrice.finalPrice)
    )
}
