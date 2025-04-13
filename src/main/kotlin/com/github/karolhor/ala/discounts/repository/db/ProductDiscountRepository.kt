package com.github.karolhor.ala.discounts.repository.db

import com.github.karolhor.ala.discounts.repository.db.model.DiscountEntity
import com.github.karolhor.ala.discounts.repository.db.model.ProductDiscountThresholdEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ProductDiscountRepository : CoroutineCrudRepository<DiscountEntity, UUID> {
    @Query(
        """
        SELECT d.product_id, d.id AS discount_id, d.policy, dt.threshold_min, dt.threshold_max, dt.discount_value
        FROM discounts d
        JOIN discounts_thresholds dt ON d.id = dt.discount_id
        WHERE d.product_id = :productId
        """,
    )
    fun findDiscountsByProductId(productId: UUID): Flow<ProductDiscountThresholdEntity>
}
