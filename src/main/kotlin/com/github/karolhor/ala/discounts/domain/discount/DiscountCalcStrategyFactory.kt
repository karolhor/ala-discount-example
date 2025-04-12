package com.github.karolhor.ala.discounts.domain.discount

import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import org.springframework.stereotype.Component

@Component
class DiscountCalcStrategyFactory {
    fun create(discount: ProductDiscount): DiscountCalcStrategy= when (discount) {
        is ProductDiscount.FixedProductDiscount -> FixedDiscountCalcStrategy(discount)
        is ProductDiscount.QuantityProductDiscount -> QuantityDiscountCalcStrategy(discount)
    }
}
