package com.github.karolhor.ala.discounts.domain.discount

import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import java.math.BigDecimal
import java.math.RoundingMode

class FixedDiscountCalcStrategy(
    private val discount: ProductDiscount.FixedProductDiscount
) : DiscountCalcStrategy {

    override fun applyDiscount(price: BigDecimal, quantity: Int): PriceDiscount {
        val totalPrice = price * quantity.toBigDecimal()
        val discountRate = discount.value.divide(PERCENTAGE_FACTOR, DISCOUNT_PRECISION, RoundingMode.HALF_UP)
        val amount = totalPrice.multiply(discountRate).setScale(PRICE_PRECISION, RoundingMode.HALF_UP)

        return PriceDiscount(
            rate = discount.value,
            amount = amount,
        )
    }

    companion object {
        private val PERCENTAGE_FACTOR = BigDecimal(100)
        private const val PRICE_PRECISION = 2
        private const val DISCOUNT_PRECISION = 4
    }
}
