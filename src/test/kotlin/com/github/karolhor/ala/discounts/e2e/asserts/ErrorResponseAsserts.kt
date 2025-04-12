package com.github.karolhor.ala.discounts.e2e.asserts

import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import assertk.assertions.startsWith
import com.github.karolhor.ala.discounts.api.error.ErrorResponse

object ErrorResponseAsserts {
    fun assertProductNotFound(errorResponse: ErrorResponse?, productId: String) {
        assertThat(errorResponse).isNotNull().all {
            prop(ErrorResponse::errors).hasSize(1)
            prop(ErrorResponse::errors).index(0).all {
                prop(ErrorResponse.Error::code).isEqualTo("PRODUCT_NOT_FOUND")
                prop(ErrorResponse.Error::message).startsWith("Product not found. Id:")
                prop(ErrorResponse.Error::message).contains(productId)
            }
        }
    }
}
