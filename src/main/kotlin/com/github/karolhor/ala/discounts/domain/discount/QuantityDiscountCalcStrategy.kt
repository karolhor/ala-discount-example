package com.github.karolhor.ala.discounts.domain.discount

import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import java.math.BigDecimal

class QuantityDiscountCalcStrategy(
    private val discount: ProductDiscount.QuantityProductDiscount
) : DiscountCalcStrategy {
    override fun applyDiscount(
        price: BigDecimal,
        quantity: Int
    ): PriceDiscount {
        val discountPercentage = discount.thresholds.first {
            quantity >= it.min && (it.max == null || quantity <= it.max)
        }.value

        if (discountPercentage == BigDecimal.ZERO) {
            return PriceDiscount.ZERO
        }

        return FixedDiscountCalcStrategy(
            ProductDiscount.FixedProductDiscount(discount.productId, discountPercentage)
        ).applyDiscount(price, quantity)
    }
}
