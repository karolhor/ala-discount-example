package com.github.karolhor.ala.discounts.domain

import com.github.karolhor.ala.discounts.domain.discount.DiscountCalcStrategyFactory
import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import com.github.karolhor.ala.discounts.domain.model.TotalPrice
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProductPriceCalculator(
    private val productDiscountsProvider: ProductDiscountsProvider,
    private val discountCalcStrategyFactory: DiscountCalcStrategyFactory
) {
    suspend fun calculateTotalPrice(product: Product, quantity: Int): TotalPrice {
        val discounts = productDiscountsProvider.findDiscountsByProductId(product.id)
        val totalPrice = product.price * quantity.toBigDecimal()
        val discountAmount = if (discounts.isEmpty()) {
            PriceDiscount.ZERO
        } else {
            discounts.maxOf { calcDiscountAmount(product, quantity, it) }
        }

        return TotalPrice(
            totalPrice = totalPrice,
            discountRate = discountAmount.rate,
            discountAmount = discountAmount.amount,
            finalPrice = totalPrice - discountAmount.amount
        )
    }

    private fun calcDiscountAmount(product: Product, quantity: Int, discount: ProductDiscount): PriceDiscount =
        discountCalcStrategyFactory
            .create(discount)
            .applyDiscount(product.price, quantity)
}
