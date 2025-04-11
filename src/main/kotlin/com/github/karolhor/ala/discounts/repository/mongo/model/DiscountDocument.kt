package com.github.karolhor.ala.discounts.repository.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "product_discounts")
data class DiscountDocument(
    @Id
    val id: String,
    val productId: String,
    val policyType: PolicyType,
    val thresholds: List<DiscountThreshold>
) {
    enum class PolicyType {
        QUANTITY, FIXED
    }

    data class DiscountThreshold(
        val min: Int?,
        val max: Int?,
        val value: Int?
    )
}
