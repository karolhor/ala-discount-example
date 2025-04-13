package com.github.karolhor.ala.discounts.domain

import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import com.github.karolhor.ala.discounts.domain.discount.DiscountCalcStrategy
import com.github.karolhor.ala.discounts.domain.discount.DiscountCalcStrategyFactory
import com.github.karolhor.ala.discounts.domain.model.PriceDiscount
import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import com.github.karolhor.ala.discounts.domain.model.TotalPrice
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.UUID

@ExtendWith(MockKExtension::class)
class ProductPriceCalculatorTest {
    @MockK
    lateinit var productDiscountsProvider: ProductDiscountsProvider

    @MockK
    lateinit var discountCalcStrategyFactory: DiscountCalcStrategyFactory

    lateinit var productPriceCalculator: ProductPriceCalculator

    @BeforeEach
    fun setUp() {
        productPriceCalculator = ProductPriceCalculator(productDiscountsProvider, discountCalcStrategyFactory)
    }

    private val product =
        Product(
            id = UUID.fromString("70c88ab4-7d08-4fe7-9f27-ada0b41c1b89"),
            name = "Example Product",
            description = "This is a description",
            price = "150.00".toBigDecimal(),
        )
    private val quantity = 5
    private val totalPrice = "750.00".toBigDecimal()
    private val baseResult =
        TotalPrice(
            discountRate = BigDecimal.ZERO,
            totalPrice = totalPrice,
            discountAmount = BigDecimal.ZERO,
            finalPrice = totalPrice,
        )

    @Test
    fun `should return total price without discount when their not given`() =
        runTest {
            // given
            coEvery { productDiscountsProvider.findDiscountsByProductId(product.id) } returns emptyList()

            // when
            val result = productPriceCalculator.calculateTotalPrice(product, quantity)

            // then
            assertThat(result).isDataClassEqualTo(baseResult)
        }

    @Test
    fun `should return total price with discount`() =
        runTest {
            // given
            val discountStrategy = mockk<DiscountCalcStrategy>()
            val discount = mockk<ProductDiscount>()
            val mockedPriceDiscount =
                PriceDiscount(
                    rate = "3.10".toBigDecimal(),
                    amount = "15.49".toBigDecimal(),
                )

            coEvery { productDiscountsProvider.findDiscountsByProductId(product.id) } returns listOf(discount)
            coEvery { discountCalcStrategyFactory.create(discount) } returns discountStrategy
            coEvery { discountStrategy.applyDiscount(product.price, quantity) } returns mockedPriceDiscount

            val expectedResult =
                baseResult.copy(
                    discountRate = mockedPriceDiscount.rate,
                    discountAmount = mockedPriceDiscount.amount,
                    finalPrice = "734.51".toBigDecimal(),
                )

            // when
            val result = productPriceCalculator.calculateTotalPrice(product, quantity)

            // then
            assertThat(result).isDataClassEqualTo(expectedResult)
        }

    @Test
    fun `should return total price with max available discount when multiple found for given product`() =
        runTest {
            // given
            val lowerDiscountStrategy = mockk<DiscountCalcStrategy>()
            val greaterDiscountStrategy = mockk<DiscountCalcStrategy>()
            val lowerValueDiscount = mockk<ProductDiscount>()
            val greaterValueDiscount = mockk<ProductDiscount>()
            val lowerPriceDiscount =
                PriceDiscount(
                    rate = "3.10".toBigDecimal(),
                    amount = "15.49".toBigDecimal(),
                )
            val greaterPriceDiscount =
                PriceDiscount(
                    rate = "7.00".toBigDecimal(),
                    amount = "31.80".toBigDecimal(),
                )

            coEvery { productDiscountsProvider.findDiscountsByProductId(product.id) } returns
                listOf(lowerValueDiscount, greaterValueDiscount)
            coEvery { discountCalcStrategyFactory.create(lowerValueDiscount) } returns lowerDiscountStrategy
            coEvery { discountCalcStrategyFactory.create(greaterValueDiscount) } returns greaterDiscountStrategy
            coEvery { lowerDiscountStrategy.applyDiscount(product.price, quantity) } returns lowerPriceDiscount
            coEvery { greaterDiscountStrategy.applyDiscount(product.price, quantity) } returns greaterPriceDiscount

            val expectedResult =
                baseResult.copy(
                    discountRate = greaterPriceDiscount.rate,
                    discountAmount = greaterPriceDiscount.amount,
                    finalPrice = "718.20".toBigDecimal(),
                )

            // when
            val result = productPriceCalculator.calculateTotalPrice(product, quantity)

            // then
            assertThat(result).isDataClassEqualTo(expectedResult)
        }
}
