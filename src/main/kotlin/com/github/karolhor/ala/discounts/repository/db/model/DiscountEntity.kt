package com.github.karolhor.ala.discounts.repository.db.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.UUID

enum class DiscountPolicy {
    QUANTITY,
    FIXED,
}

@Table(name = "discounts")
data class DiscountEntity(
    @Id
    val id: UUID,
    @Column("product_id")
    val productId: UUID,
    val policy: DiscountPolicy,
)

@Table(name = "discounts_thresholds")
data class DiscountThresholdEntity(
    @Id
    val id: UUID,
    @Column("discount_id")
    val discountId: UUID,
    @Column("threshold_min")
    val min: Int? = null,
    @Column("threshold_max")
    val max: Int? = null,
    @Column("discount_value")
    val value: BigDecimal,
)

data class ProductDiscountThresholdEntity(
    @Column("product_id")
    val productId: UUID,
    @Column("discount_id")
    val discountId: UUID,
    val policy: DiscountPolicy,
    @Column("threshold_min")
    val min: Int? = null,
    @Column("threshold_max")
    val max: Int? = null,
    @Column("discount_value")
    val value: BigDecimal,
)
