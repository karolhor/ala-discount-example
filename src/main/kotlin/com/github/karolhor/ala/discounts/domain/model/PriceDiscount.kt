package com.github.karolhor.ala.discounts.domain.model

import java.math.BigDecimal

data class PriceDiscount(
    val rate: BigDecimal,
    val amount: BigDecimal,
) : Comparable<PriceDiscount> {
    override fun compareTo(other: PriceDiscount): Int = amount.compareTo(other.amount)

    companion object {
        val ZERO = PriceDiscount(BigDecimal.ZERO, BigDecimal.ZERO)
    }
}
