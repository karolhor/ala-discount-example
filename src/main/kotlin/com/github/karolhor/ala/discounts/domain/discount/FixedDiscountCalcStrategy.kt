package com.github.karolhor.ala.discounts.domain.discount

import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import io.github.oshai.kotlinlogging.KotlinLogging
import java.math.BigDecimal
import java.math.RoundingMode

private val logger = KotlinLogging.logger {}

class FixedDiscountCalcStrategy(
    private val discountRate: BigDecimal,
) : DiscountCalcStrategy {
    override fun applyDiscount(
        price: BigDecimal,
        quantity: Int,
    ): PriceDiscount {
        val totalPrice = price * quantity.toBigDecimal()

        if (discountRate >= MAX_DISCOUNT) {
            logger.warn { "Provided discount rate $discountRate is too large. Applied max available: $MAX_DISCOUNT." }
            return PriceDiscount(
                rate = MAX_DISCOUNT,
                amount = totalPrice,
            )
        }

        val rateAsDecimalFraction = discountRate.divide(PERCENTAGE_FACTOR, DISCOUNT_PRECISION, RoundingMode.HALF_UP)
        val amount = totalPrice.multiply(rateAsDecimalFraction).withPriceScale()

        return PriceDiscount(
            rate = discountRate,
            amount = amount,
        )
    }

    companion object {
        private val PERCENTAGE_FACTOR = BigDecimal(100)
        private const val DISCOUNT_PRECISION = 4
        private val MAX_DISCOUNT = BigDecimal(100).withPriceScale()
    }
}

private const val PRICE_PRECISION = 2

private fun BigDecimal.withPriceScale() = this.setScale(PRICE_PRECISION, RoundingMode.HALF_UP)
