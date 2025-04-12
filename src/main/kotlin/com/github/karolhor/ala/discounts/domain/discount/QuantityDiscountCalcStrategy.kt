package com.github.karolhor.ala.discounts.domain.discount

import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import java.math.BigDecimal
import java.math.RoundingMode

class QuantityDiscountCalcStrategy(
    private val discount: ProductDiscount.QuantityProductDiscount
): DiscountCalcStrategy {
    override fun applyDiscount(
        price: BigDecimal,
        quantity: Int
    ): PriceDiscount {
        val totalPrice = price * quantity.toBigDecimal()

        val discountPercentage = discount.thresholds.first {
            quantity >= it.min && (it.max == null || quantity <= it.max)
        }.value

        if (discountPercentage == BigDecimal.ZERO) {
            return PriceDiscount.ZERO
        }

        val discountRate = discountPercentage.divide(PERCENTAGE_FACTOR, PRICE_PRECISION, RoundingMode.HALF_UP)
        val amount = totalPrice * discountRate

        return PriceDiscount(discountPercentage, amount)
    }

    companion object {
        private val PERCENTAGE_FACTOR = BigDecimal(100)
        private const val PRICE_PRECISION = 2
    }
}
