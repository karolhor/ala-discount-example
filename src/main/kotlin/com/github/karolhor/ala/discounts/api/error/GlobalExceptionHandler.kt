package com.github.karolhor.ala.discounts.api.error

import com.github.karolhor.ala.discounts.domain.ProductNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.MissingRequestValueException

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

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleQueryParamsValidationExceptions(ex: ConstraintViolationException): ErrorResponse {
        val errors = ex.constraintViolations.map {
            val path = it.propertyPath.toString()
            val field = path.substringAfterLast('.')
            ErrorResponse.Error(
                code = CODE_VALIDATION_ERROR,
                message = it.message.orEmpty(),
                field = field
            )
        }
        return ErrorResponse(errors)
    }

    @ExceptionHandler(MissingRequestValueException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingQueryParamException(ex: MissingRequestValueException): ErrorResponse {
        return ErrorResponse.withSingleError(
            ErrorResponse.Error(
                code = CODE_VALIDATION_ERROR,
                message = "missing required query parameter",
                field = ex.name
            )
        )
    }

    companion object {
        const val CODE_VALIDATION_ERROR = "VALIDATION_ERROR"
    }
}
