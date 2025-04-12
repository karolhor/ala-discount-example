package com.github.karolhor.ala.discounts.api

import com.github.karolhor.ala.discounts.api.mapper.ProductApiMapper
import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.api.model.TotalProductPriceResponse
import com.github.karolhor.ala.discounts.domain.ProductPriceCalculator
import com.github.karolhor.ala.discounts.domain.ProductProvider
import jakarta.validation.constraints.Min
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Validated
@RestController
@RequestMapping("/v1/products")
class ProductApiEndpoint(
    private val productApiMapper: ProductApiMapper,
    private val productProvider: ProductProvider,
    private val productPriceCalculator: ProductPriceCalculator
) {

    @GetMapping("/{id}")
    suspend fun getProductDetails(@PathVariable id: UUID): ProductResponse {
        val product = productProvider.getProductById(id)
        return productApiMapper.productToResponse(product)
    }

    @GetMapping("/{id}/total-price")
    suspend fun calcTotalPrice(
        @PathVariable id: UUID,
        @RequestParam(value = "quantity", required = true) @Min(1) quantity: Int
    ): TotalProductPriceResponse {
        val product = productProvider.getProductById(id)
        val totalPrice = productPriceCalculator.calculateTotalPrice(product, quantity)

        return productApiMapper.totalPriceToResponse(product, quantity, totalPrice)
    }
}
