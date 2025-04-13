package com.github.karolhor.ala.discounts.e2e

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.github.karolhor.ala.discounts.api.error.ErrorResponse
import com.github.karolhor.ala.discounts.api.model.TotalProductPriceResponse
import com.github.karolhor.ala.discounts.e2e.asserts.ErrorResponseAsserts.assertProductNotFound
import com.github.karolhor.ala.discounts.fixtures.ProductFixtures
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.test.web.reactive.server.expectBody

class GetApiTotalPriceProductIntegrationTest : IntegrationTest() {
    @BeforeEach
    fun setUp() {
        runTest {
            ProductFixtures.allEntities.forEach {
                entityTemplate.insert(it).awaitSingle()
            }
        }
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        nullValues = ["null"],
        value = [
            "-1   | must be greater than or equal to 1",
            "0    | must be greater than or equal to 1",
            "null | missing required query parameter",
        ]
    )
    fun `should return 400 when quantity has invalid value`(quantity: Int?, errorMessage: String) {
        // given
        val unknownProductId = "58bb4acf-4d49-40cf-ac06-6573fce400ae"
        val url =
            quantity?.let { getProductTotalPriceUrl(unknownProductId, it) } ?: getProductTotalPriceUrlWithoutQuantity(
                unknownProductId
            )

        // when
        val response = webTestClient.get()
            .uri(url)
            .exchange()

        // then
        val errorsBody = response
            .expectStatus().isBadRequest
            .expectBody<ErrorResponse>()
            .returnResult()
            .responseBody


        // and
        assertThat(errorsBody).isNotNull().all {
            prop(ErrorResponse::errors).hasSize(1)
            prop(ErrorResponse::errors).index(0).all {
                prop(ErrorResponse.Error::code).isEqualTo("VALIDATION_ERROR")
                prop(ErrorResponse.Error::message).isEqualTo(errorMessage)
                prop(ErrorResponse.Error::field).isEqualTo("quantity")
            }
        }
    }

    @Test
    fun `should return 404 when product does not exists`() {
        // given
        val unknownProductId = "58bb4acf-4d49-40cf-ac06-6573fce400ae"

        // when
        val response = webTestClient.get()
            .uri(getProductTotalPriceUrl(unknownProductId, 11))
            .exchange()

        // then
        val errorsBody = response
            .expectStatus().isNotFound
            .expectBody<ErrorResponse>()
            .returnResult()
            .responseBody

        // and
        assertProductNotFound(errorsBody, unknownProductId)
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = [
            "7b34d710-b928-4a6d-a0cb-f7565b95fb66 | 1  | 0.00  | 0.00     | 1765.50   | 1765.50", // no discount,
            "7b34d710-b928-4a6d-a0cb-f7565b95fb66 | 5  | 0.00  | 0.00     | 8827.50   | 8827.50", // no discount,
            "7b34d710-b928-4a6d-a0cb-f7565b95fb66 | 8  | 0.00  | 0.00     | 14124.00  | 14124.00", // no discount,
            "893ce0db-842e-4418-9e5c-aca7abe43e56 | 1  | 0.00  | 0.00     | 9231.99   | 9231.99", // only quantity
            "893ce0db-842e-4418-9e5c-aca7abe43e56 | 7  | 5.00  | 3231.20  | 64623.93  | 61392.73", // only quantity
            "893ce0db-842e-4418-9e5c-aca7abe43e56 | 25 | 15.50 | 35773.96 | 230799.75 | 195025.79", // only quantity
            "cff454f7-3f76-4215-a5e7-5846a01ebe3b | 1  | 2.50  | 3.75     | 150.00    | 146.25", // only fixed
            "cff454f7-3f76-4215-a5e7-5846a01ebe3b | 99 | 2.50  | 371.25   | 14850.00  | 14478.75", // only fixed
            "39fa0600-9590-4910-a5a4-acd7abc96f2d | 1  | 5.00  | 27.20    | 544.00    | 516.80", // mixed
            "39fa0600-9590-4910-a5a4-acd7abc96f2d | 7  | 5.00  | 190.40   | 3808.00   | 3617.60", // mixed
            "39fa0600-9590-4910-a5a4-acd7abc96f2d | 12 | 8.00  | 522.24   | 6528.00   | 6005.76", // mixed
        ]
    )
    fun `should calculate total price`(
        productId: String,
        quantity: Int,
        discountRate: String,
        discountAmount: String,
        basePrice: String,
        finalPrice: String
    ) {
        // when
        val response = webTestClient.get()
            .uri(getProductTotalPriceUrl(productId, quantity))
            .exchange()

        // then
        val body = response
            .expectStatus().isOk
            .expectBody<TotalProductPriceResponse>()
            .returnResult()
            .responseBody

        assertThat(body).isNotNull().all {
            prop(TotalProductPriceResponse::discount).all {
                prop(TotalProductPriceResponse.DiscountValues::rate).isEqualTo(discountRate)
                prop(TotalProductPriceResponse.DiscountValues::amount).isEqualTo(discountAmount)
            }
            prop(TotalProductPriceResponse::totalPrice).all {
                prop(TotalProductPriceResponse.PriceValues::base).isEqualTo(basePrice)
                prop(TotalProductPriceResponse.PriceValues::final).isEqualTo(finalPrice)
            }
        }

    }


    private fun getProductTotalPriceUrl(productId: String, quantity: Int) =
        "/v1/products/$productId/total-price?quantity=$quantity"

    private fun getProductTotalPriceUrlWithoutQuantity(productId: String) = "/v1/products/$productId/total-price"
}
