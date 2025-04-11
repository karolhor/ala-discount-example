package com.github.karolhor.ala.discounts.repository.db.mapper

import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.repository.db.model.ProductEntity
import org.springframework.stereotype.Component

@Component
class ProductEntityMapper {
    fun toDomain(entity: ProductEntity) = Product(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        price = entity.price,
    )
}
