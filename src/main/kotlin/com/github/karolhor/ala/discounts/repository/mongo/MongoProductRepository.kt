package com.github.karolhor.ala.discounts.repository.mongo

import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.repository.ProductRepository
import com.github.karolhor.ala.discounts.repository.mongo.mapper.ProductDocumentMapper
import com.github.karolhor.ala.discounts.repository.mongo.model.ProductDocument
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findById
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MongoProductRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val mapper: ProductDocumentMapper
) : ProductRepository {

    override suspend fun findProductById(productId: UUID): Product? =
        try {
            mongoTemplate
                .findById<ProductDocument>(productId.toString())
                .awaitSingleOrNull()
                ?.let { mapper.toDomain(it) }
        } catch (ex: Exception) {
            throw MongoProductRepositoryException("Could not find product with id $productId", ex)
        }
}

class MongoProductRepositoryException(message: String, cause: Throwable) : Exception(message, cause)
