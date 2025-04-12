package com.github.karolhor.ala.discounts.domain

import com.github.karolhor.ala.discounts.domain.model.ProductDiscount
import com.github.karolhor.ala.discounts.repository.db.ProductDiscountRepository
import com.github.karolhor.ala.discounts.repository.db.model.DiscountPolicy
import com.github.karolhor.ala.discounts.repository.db.model.ProductDiscountThresholdEntity
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class ProductDiscountsProvider(
    private val discountRepository: ProductDiscountRepository
) {
    suspend fun findDiscountsByProductId(id: UUID): List<ProductDiscount> {
        val discountThresholds = discountRepository
            .findDiscountsByProductId(id)
            .toList()
            .groupBy { it.discountId }
            .filter { (_, thresholds) -> thresholds.isNotEmpty() }
            .mapNotNull { mapDiscounts(it) }

        return discountThresholds
    }

    private fun mapDiscounts(entry: Map.Entry<UUID, List<ProductDiscountThresholdEntity>>): ProductDiscount? =
        when (entry.value.first().policy) {
            DiscountPolicy.QUANTITY -> mapQuantityDiscount(entry.value)
            DiscountPolicy.FIXED -> mapFixedDiscount(entry.value.first())
        }

    private fun mapQuantityDiscount(discountThresholds: List<ProductDiscountThresholdEntity>) =
        ProductDiscount.QuantityProductDiscount(
            productId = discountThresholds.first().productId,
            thresholds = discountThresholds
                .filter { it.min != null}
                .sortedBy { it.min }
                .map {
                    ProductDiscount.QuantityProductDiscount.DiscountThreshold(
                        min = it.min!!,
                        max = it.max,
                        value = it.value
                    )
                }
        )

    private fun mapFixedDiscount(discountEntity: ProductDiscountThresholdEntity): ProductDiscount.FixedProductDiscount? {
        return if (discountEntity.value == BigDecimal.ZERO) {
            // logg warn
            null
        } else {
            ProductDiscount.FixedProductDiscount(
                productId = discountEntity.productId,
                value = discountEntity.value
            )
        }
    }
}
