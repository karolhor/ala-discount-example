package com.github.karolhor.ala.discounts.e2e

import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isNotNull
import com.github.karolhor.ala.discounts.api.error.ErrorResponse
import com.github.karolhor.ala.discounts.api.model.ProductResponse
import com.github.karolhor.ala.discounts.e2e.asserts.ErrorResponseAsserts.assertProductNotFound
import com.github.karolhor.ala.discounts.fixtures.ProductFixtures
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.expectBody

class GetApiProductIntegrationTest : IntegrationTest() {
    private val product = ProductFixtures.noDiscountProductEntity

    @BeforeEach
    fun setUp() {
        runTest {
            entityTemplate.insert(product).awaitSingle()
        }
    }

    @Test
    fun `should return 404 when product does not exists`() {
        // given
        val unknownProductId = "58bb4acf-4d49-40cf-ac06-6573fce400ae"

        // when
        val response =
            webTestClient
                .get()
                .uri(getProductUrl(unknownProductId))
                .exchange()

        // then
        val errorsBody =
            response
                .expectStatus()
                .isNotFound
                .expectBody<ErrorResponse>()
                .returnResult()
                .responseBody

        // and
        assertProductNotFound(errorsBody, unknownProductId)
    }

    @Test
    fun `should return product by id`() {
        // given
        val expectedResponse =
            ProductResponse(
                id = product.id,
                name = product.name,
                description = product.description,
                price = "1765.50",
            )
        // when
        val response =
            webTestClient
                .get()
                .uri(getProductUrl(product.id.toString()))
                .exchange()

        // then
        val body =
            response
                .expectStatus()
                .isOk
                .expectBody<ProductResponse>()
                .returnResult()
                .responseBody

        assertThat(body)
            .isNotNull()
            .isDataClassEqualTo(expectedResponse)
    }

    private fun getProductUrl(productId: String) = "/v1/products/$productId"
}
