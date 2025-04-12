package com.github.karolhor.ala.discounts.domain.discount

import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import java.math.BigDecimal

interface DiscountCalcStrategy {
    fun applyDiscount(price: BigDecimal, quantity: Int): PriceDiscount
}
