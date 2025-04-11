package com.github.karolhor.ala.discounts.api.error

import com.github.karolhor.ala.discounts.domain.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handlePatientNotFoundExceptions(ex: Exception): ErrorResponse {
        return ErrorResponse.withSingleError(
            ErrorResponse.Error(
                code = "PRODUCT_NOT_FOUND",
                message = ex.message.orEmpty()
            )
        )
    }
}
