package com.github.karolhor.ala.discounts.domain.discount

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount.QuantityProductDiscount.DiscountThreshold
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

class QuantityDiscountCalcStrategyTest {

    @Test
    fun `should return 0 when there is no threshold discount for given quantity`() {
        // given
        val thresholds = listOf(
            DiscountThreshold(
                min = 5,
                max = 11,
                value = BigDecimal.ONE
            ),
            DiscountThreshold(
                min = 12,
                max = 15,
                value = BigDecimal.TWO
            )
        )

        // when
        val result = QuantityDiscountCalcStrategy(thresholds).applyDiscount(BigDecimal.TEN, 3)

        // then
        assertThat(result.rate).isEqualTo(BigDecimal.ZERO)
        assertThat(result.amount).isEqualTo(BigDecimal.ZERO)
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = [
            "5    | 2.00 | 12.31",
            "7    | 2.00 | 17.24",
            "11   | 2.00 | 27.08",
            "12   | 3.00 | 44.32",
            "15   | 3.00 | 55.40",
            "16   | 4.00 | 78.79",
            "18   | 4.00 | 88.64",
            "1001 | 4.00 | 4929.32",
        ]
    )
    fun `should apply discount from selected threshold by quantity`(quantity: Int, discountRate: BigDecimal, expectedDiscountAmount: BigDecimal) {
        // given
        val thresholds = listOf(
            DiscountThreshold(
                min = 5,
                max = 11,
                value = "2.00".toBigDecimal(),
            ),
            DiscountThreshold(
                min = 12,
                max = 15,
                value = "3.00".toBigDecimal(),
            ),
            DiscountThreshold(
                min = 16,
                max = null,
                value = "4.00".toBigDecimal(),
            )
        )
        val price = "123.11".toBigDecimal()

        // when
        val result = QuantityDiscountCalcStrategy(thresholds).applyDiscount(price, quantity)

        // then
        assertThat(result.rate).isEqualTo(discountRate)
        assertThat(result.amount).isEqualTo(expectedDiscountAmount)
    }
}
