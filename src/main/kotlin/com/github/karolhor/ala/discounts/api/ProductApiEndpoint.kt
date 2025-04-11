package com.github.karolhor.ala.discounts.api

import com.github.karolhor.ala.discounts.api.mapper.ProductApiMapper
import com.github.karolhor.ala.discounts.api.model.Price
import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.api.model.TotalProductPriceResponse
import com.github.karolhor.ala.discounts.domain.ProductDiscountsProvider
import com.github.karolhor.ala.discounts.domain.ProductPriceCalculator
import com.github.karolhor.ala.discounts.domain.ProductProvider
import com.github.karolhor.ala.discounts.repository.db.ProductDiscountRepository
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import kotlinx.coroutines.flow.toList
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

        return TotalProductPriceResponse(
            productId = id,
            quantity = quantity,
            totalPrice = Price(
                inCents = 10,
                formatted = "10.00"
            )
        )
    }
}
