package com.github.karolhor.ala.discounts.api.error

data class ErrorResponse(
    val errors: List<Error>
) {
    data class Error(
        val code: String,
        val message: String,
        val field: String? = null
    )

    companion object {
        fun withSingleError(error: Error) = ErrorResponse(errors = listOf(error))
    }
}
