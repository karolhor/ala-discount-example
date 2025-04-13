package com.github.karolhor.ala.discounts.fixtures

import com.github.karolhor.ala.discounts.repository.db.model.DiscountEntity
import com.github.karolhor.ala.discounts.repository.db.model.DiscountPolicy
import com.github.karolhor.ala.discounts.repository.db.model.DiscountThresholdEntity
import com.github.karolhor.ala.discounts.repository.db.model.ProductEntity
import java.math.BigDecimal
import java.util.UUID

object ProductFixtures {
    val noDiscountProductEntity =
        ProductEntity(
            id = UUID.fromString("7b34d710-b928-4a6d-a0cb-f7565b95fb66"),
            name = "Test product - no discount",
            description = "Test description",
            price = 1765.50.toBigDecimal(),
        )

    val quantityDiscountProductEntity =
        ProductEntity(
            id = UUID.fromString("893ce0db-842e-4418-9e5c-aca7abe43e56"),
            name = "Test product - quantity discount",
            description = "Test description",
            price = 9231.99.toBigDecimal(),
        )

    val fixedDiscountProductEntity =
        ProductEntity(
            id = UUID.fromString("cff454f7-3f76-4215-a5e7-5846a01ebe3b"),
            name = "Test product - fixed discount",
            description = "Test description",
            price = 150.00.toBigDecimal(),
        )

    val mixedDiscountProductEntity =
        ProductEntity(
            id = UUID.fromString("39fa0600-9590-4910-a5a4-acd7abc96f2d"),
            name = "Test product - mixed discount",
            description = "Test description",
            price = 544.00.toBigDecimal(),
        )

    val quantityDiscountEntity =
        DiscountEntity(
            id = UUID.fromString("b531c341-937f-4b46-9496-63ae786e8eff"),
            productId = quantityDiscountProductEntity.id,
            policy = DiscountPolicy.QUANTITY,
        )

    val fixedDiscountEntity =
        DiscountEntity(
            id = UUID.fromString("91b198db-3aff-441e-8ad0-17477fb72c60"),
            productId = fixedDiscountProductEntity.id,
            policy = DiscountPolicy.FIXED,
        )

    val quantityForMixedDiscountEntity =
        DiscountEntity(
            id = UUID.fromString("7840c976-f05e-4db5-ba02-c1d1be924db8"),
            productId = mixedDiscountProductEntity.id,
            policy = DiscountPolicy.QUANTITY,
        )

    val fixedForMixedDiscountEntity =
        DiscountEntity(
            id = UUID.fromString("c12f92e0-0afb-4550-8da8-440922dace92"),
            productId = mixedDiscountProductEntity.id,
            policy = DiscountPolicy.FIXED,
        )

    val discountThreshold1to5Entity =
        DiscountThresholdEntity(
            id = UUID.fromString("8e3d3e2b-c5f9-40d4-a215-1620884be0c6"),
            discountId = quantityDiscountEntity.id,
            min = 1,
            max = 5,
            value = BigDecimal.ZERO,
        )

    val discountThreshold6to12Entity =
        DiscountThresholdEntity(
            id = UUID.fromString("d4907eca-d339-4edf-85b1-dab2dcf9e5fd"),
            discountId = quantityDiscountEntity.id,
            min = 6,
            max = 12,
            value = 5.toBigDecimal(),
        )

    val discountThreshold13toInfEntity =
        DiscountThresholdEntity(
            id = UUID.fromString("21cd983f-f2d7-413b-81d7-eb5dad69ccb1"),
            discountId = quantityDiscountEntity.id,
            min = 13,
            max = null,
            value = 15.5.toBigDecimal(),
        )

    val fixedThresholdEntity =
        DiscountThresholdEntity(
            id = UUID.fromString("cf8f167d-983d-4a7b-9c5a-a78ee6447472"),
            discountId = fixedDiscountEntity.id,
            min = null,
            max = null,
            value = 2.5.toBigDecimal(),
        )

    val mixedQuantityThresholdEntity =
        DiscountThresholdEntity(
            id = UUID.fromString("aabed3a6-b79f-4898-a954-52c4dc39b803"),
            discountId = quantityForMixedDiscountEntity.id,
            min = 12,
            max = null,
            value = 8.toBigDecimal(),
        )

    val mixedFixedThresholdEntity =
        DiscountThresholdEntity(
            id = UUID.fromString("0caca8f3-b073-49a2-80d7-8650fe1c7d09"),
            discountId = fixedForMixedDiscountEntity.id,
            min = null,
            max = null,
            value = 5.toBigDecimal(),
        )

    val allEntities =
        listOf(
            noDiscountProductEntity,
            quantityDiscountProductEntity,
            fixedDiscountProductEntity,
            mixedDiscountProductEntity,
            quantityDiscountEntity,
            fixedDiscountEntity,
            quantityForMixedDiscountEntity,
            fixedForMixedDiscountEntity,
            discountThreshold1to5Entity,
            discountThreshold6to12Entity,
            discountThreshold13toInfEntity,
            fixedThresholdEntity,
            mixedQuantityThresholdEntity,
            mixedFixedThresholdEntity,
        )
}
