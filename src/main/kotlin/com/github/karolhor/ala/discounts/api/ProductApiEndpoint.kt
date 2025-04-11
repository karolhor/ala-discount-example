package com.github.karolhor.ala.discounts.api

import com.github.karolhor.ala.discounts.api.mapper.ProductApiMapper
import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.domain.ProductProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/products")
class ProductApiEndpoint(
    private val productApiMapper: ProductApiMapper,
    private val productProvider: ProductProvider
) {

    @GetMapping("/{id}")
    suspend fun getProductDetails(@PathVariable id: UUID): ProductResponse {
        val product = productProvider.getProductById(id)
        return productApiMapper.productToResponse(product)
    }
}
