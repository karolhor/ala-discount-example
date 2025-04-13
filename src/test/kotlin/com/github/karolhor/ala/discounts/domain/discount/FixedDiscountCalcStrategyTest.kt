package com.github.karolhor.ala.discounts.domain.discount

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

class FixedDiscountCalcStrategyTest {
    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = [
            "0.00   | 0.00   | 13  | 0.00",
            "0.00   | 12.49  | 7   | 0.00",
            "2.00   | 10.00  | 13  | 2.60",
            "5.00   | 35.11  | 451 | 791.73", // rounding down from 791,7305
            "11.50  | 45.99  | 35  | 185.11", // rounding up from 185,10975
            "100.00 | 131.21 | 45  | 5904.45",
        ],
    )
    fun `should apply discount`(
        discountRate: BigDecimal,
        price: BigDecimal,
        quantity: Int,
        expectedDiscountAmount: BigDecimal,
    ) {
        // when
        val result = FixedDiscountCalcStrategy(discountRate).applyDiscount(price, quantity)

        // then
        assertThat(result.rate).isEqualTo(discountRate)
        assertThat(result.amount).isEqualTo(expectedDiscountAmount)
    }

    @Test
    fun `should limit discount to 100 max`() {
        // when
        val result = FixedDiscountCalcStrategy("101.00".toBigDecimal()).applyDiscount("150.00".toBigDecimal(), 3)

        // then
        assertThat(result.rate).isEqualTo("100.00".toBigDecimal())
        assertThat(result.amount).isEqualTo("450.00".toBigDecimal())
    }
}
