package com.github.karolhor.ala.discounts.domain.model

import java.math.BigDecimal
import java.util.UUID

sealed interface ProductDiscount {
    val productId: UUID

    data class QuantityProductDiscount(
        override val productId: UUID,
        val thresholds: List<DiscountThreshold>,
    ) : ProductDiscount {
        data class DiscountThreshold(
            val min: Int,
            val max: Int?,
            val value: BigDecimal,
        )
    }

    data class FixedProductDiscount(
        override val productId: UUID,
        val value: BigDecimal,
    ) : ProductDiscount
}
