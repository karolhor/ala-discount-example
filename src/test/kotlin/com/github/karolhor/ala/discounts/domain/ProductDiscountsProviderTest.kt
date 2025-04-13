package com.github.karolhor.ala.discounts.domain

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import com.github.karolhor.ala.discounts.repository.db.ProductDiscountRepository
import com.github.karolhor.ala.discounts.repository.db.model.DiscountPolicy
import com.github.karolhor.ala.discounts.repository.db.model.ProductDiscountThresholdEntity
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class ProductDiscountsProviderTest {
    @MockK
    lateinit var discountRepository: ProductDiscountRepository

    lateinit var discountsProvider: ProductDiscountsProvider

    @BeforeEach
    fun setUp() {
        discountsProvider = ProductDiscountsProvider(discountRepository)
    }

    private val productId = UUID.fromString("527bb846-f504-44f7-9879-02f5962fc705")
    private val discountId = UUID.fromString("2bd0963b-3154-4ea2-b23c-7ac8c1235648")
    private val baseDiscountEntity =
        ProductDiscountThresholdEntity(
            productId = productId,
            discountId = discountId,
            policy = DiscountPolicy.FIXED,
            min = null,
            max = null,
            value = "5.00".toBigDecimal(),
        )

    @Test
    fun `should convert fixed discount entries to proper domain object`() =
        runTest {
            // given
            coEvery { discountRepository.findDiscountsByProductId(productId) } returns flowOf(baseDiscountEntity)

            // when
            val result = discountsProvider.findDiscountsByProductId(productId)

            // then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isDataClassEqualTo(
                ProductDiscount.FixedProductDiscount(
                    productId = productId,
                    value = baseDiscountEntity.value,
                ),
            )
        }

    @Test
    fun `should ignore entity when value is 0`() =
        runTest {
            // given
            val invalidEntity =
                baseDiscountEntity.copy(
                    value = BigDecimal.ZERO,
                )
            coEvery { discountRepository.findDiscountsByProductId(productId) } returns flowOf(invalidEntity)

            // when
            val result = discountsProvider.findDiscountsByProductId(productId)

            // then
            assertThat(result).isEmpty()
        }

    @Test
    fun `should convert quantity discount entries to proper domain object`() =
        runTest {
            // given
            val thresholdOne =
                baseDiscountEntity.copy(
                    policy = DiscountPolicy.QUANTITY,
                    min = 13,
                    max = 15,
                    value = "5.00".toBigDecimal(),
                )
            val thresholdTwo =
                baseDiscountEntity.copy(
                    policy = DiscountPolicy.QUANTITY,
                    min = 3,
                    max = 12,
                    value = "2.00".toBigDecimal(),
                )
            val thresholdThree =
                baseDiscountEntity.copy(
                    policy = DiscountPolicy.QUANTITY,
                    min = 16,
                    max = null,
                    value = "9.00".toBigDecimal(),
                )

            val thresholdFour =
                baseDiscountEntity.copy(
                    policy = DiscountPolicy.QUANTITY,
                    min = null,
                    max = 5,
                    value = "1.00".toBigDecimal(),
                )
            coEvery { discountRepository.findDiscountsByProductId(productId) } returns
                flowOf(
                    thresholdOne,
                    thresholdTwo,
                    thresholdThree,
                    thresholdFour,
                )

            // when
            val result = discountsProvider.findDiscountsByProductId(productId)

            // then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isDataClassEqualTo(
                ProductDiscount.QuantityProductDiscount(
                    productId = productId,
                    thresholds =
                        listOf(
                            mapEntityThresholdToDomain(thresholdTwo),
                            mapEntityThresholdToDomain(thresholdOne),
                            mapEntityThresholdToDomain(thresholdThree),
                        ),
                ),
            )
        }

    @Test
    fun `should provide and convert multiple discounts`() =
        runTest {
            // given
            val quantityDiscountEntity =
                baseDiscountEntity.copy(
                    discountId = UUID.fromString("61fdf5be-568d-4403-b0a6-59ba33e82e86"),
                    policy = DiscountPolicy.QUANTITY,
                    min = 13,
                    max = 15,
                    value = "5.00".toBigDecimal(),
                )
            coEvery { discountRepository.findDiscountsByProductId(productId) } returns
                flowOf(
                    baseDiscountEntity,
                    quantityDiscountEntity,
                )

            // when
            val result = discountsProvider.findDiscountsByProductId(productId)

            // then
            assertThat(result).hasSize(2)
            assertThat(result[0]).isDataClassEqualTo(
                ProductDiscount.FixedProductDiscount(
                    productId = productId,
                    value = baseDiscountEntity.value,
                ),
            )
            assertThat(result[1]).isDataClassEqualTo(
                ProductDiscount.QuantityProductDiscount(
                    productId = productId,
                    thresholds =
                        listOf(
                            mapEntityThresholdToDomain(quantityDiscountEntity),
                        ),
                ),
            )
        }

    private fun mapEntityThresholdToDomain(entity: ProductDiscountThresholdEntity) =
        ProductDiscount.QuantityProductDiscount.DiscountThreshold(
            min = entity.min!!,
            max = entity.max,
            value = entity.value,
        )
}
