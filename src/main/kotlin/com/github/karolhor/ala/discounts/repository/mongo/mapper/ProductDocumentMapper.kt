package com.github.karolhor.ala.discounts.repository.mongo.mapper

import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.repository.mongo.model.ProductDocument
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductDocumentMapper {
    fun toDomain(productDocument: ProductDocument) = Product(
        id = UUID.fromString(productDocument.id),
        name = productDocument.name,
        description = productDocument.description,
        price = productDocument.price,
    )
}
