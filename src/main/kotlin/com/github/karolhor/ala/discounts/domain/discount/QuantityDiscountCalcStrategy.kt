package com.github.karolhor.ala.discounts.domain.discount

import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount.QuantityProductDiscount.DiscountThreshold
import java.math.BigDecimal

class QuantityDiscountCalcStrategy(
    private val discountThresholds: List<DiscountThreshold>,
) : DiscountCalcStrategy {
    override fun applyDiscount(
        price: BigDecimal,
        quantity: Int,
    ): PriceDiscount {
        val discountPercentage =
            discountThresholds
                .firstOrNull {
                    quantity >= it.min && (it.max == null || quantity <= it.max)
                }?.value

        if (discountPercentage == null || discountPercentage == BigDecimal.ZERO) {
            return PriceDiscount.ZERO
        }

        return FixedDiscountCalcStrategy(discountPercentage).applyDiscount(price, quantity)
    }
}
