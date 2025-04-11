package com.github.karolhor.ala.discounts.e2e

import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import assertk.assertions.startsWith
import com.github.karolhor.ala.discounts.api.error.ErrorResponse
import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.repository.mongo.model.ProductDocument
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.expectBody
import java.util.UUID

class GetApiProductIntegrationTest : IntegrationTest() {
    private val product = ProductDocument(
        id = "7b34d710-b928-4a6d-a0cb-f7565b95fb66",
        name = "Test product",
        description = "Test description",
        price = 176550
    )

    @BeforeEach
    fun setUp() {
        runTest {
            insertProductDocument(product)
        }
    }

    @Test
    fun `should return 404 when product does not exists`() {
        // given
        val unknownProductId = "58bb4acf-4d49-40cf-ac06-6573fce400ae"

        // when
        val response = webTestClient.get()
            .uri(getProductUrl(unknownProductId))
            .exchange()

        // then
        val errorsBody = response
            .expectStatus().isNotFound
            .expectBody<ErrorResponse>()
            .returnResult()
            .responseBody

        // and
        assertThat(errorsBody).isNotNull().all {
            prop(ErrorResponse::errors).hasSize(1)
            prop(ErrorResponse::errors).index(0).all {
                prop(ErrorResponse.Error::code).isEqualTo("PRODUCT_NOT_FOUND")
                prop(ErrorResponse.Error::message).startsWith("Product not found. Id:")
                prop(ErrorResponse.Error::message).contains(unknownProductId)
            }
        }
    }

    @Test
    fun `should return product by id`() {
        // given
        val expectedResponse = ProductResponse(
            id = UUID.fromString(product.id),
            name = product.name,
            description = product.description,
            priceInCents = product.price,
            price = "1765.50"
        )
        // when
        val response = webTestClient.get()
            .uri(getProductUrl(product.id))
            .exchange()

        // then
        val body = response
            .expectStatus().isOk
            .expectBody<ProductResponse>()
            .returnResult()
            .responseBody

        assertThat(body)
            .isNotNull()
            .isDataClassEqualTo(expectedResponse)
    }

    private suspend fun insertProductDocument(productDocument: ProductDocument) {
        mongoTemplate.insert(productDocument).awaitSingle()
    }

    private fun getProductUrl(productId: String) = "/v1/products/$productId"
}
