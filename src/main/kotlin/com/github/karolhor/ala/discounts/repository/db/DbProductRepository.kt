package com.github.karolhor.ala.discounts.repository.db

import com.github.karolhor.ala.discounts.domain.ProductRepository
import com.github.karolhor.ala.discounts.domain.model.Product
import com.github.karolhor.ala.discounts.repository.db.mapper.ProductEntityMapper
import com.github.karolhor.ala.discounts.repository.db.model.ProductEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID


interface ProductEntityRepository : CoroutineCrudRepository<ProductEntity, UUID>

@Repository
class DbProductRepository(
    private val mapper: ProductEntityMapper,
    private val entityRepository: ProductEntityRepository
) : ProductRepository {
    override suspend fun findProductById(productId: UUID): Product? =
        try {
            entityRepository
                .findById(productId)
                ?.let { mapper.toDomain(it) }

        } catch (ex: Exception) {
            throw ProductRepositoryException("Could not fetch product by id: $productId", ex)
        }
}

class ProductRepositoryException(message: String, override val cause: Throwable?) : RuntimeException(message)
