package com.example

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory

/**
 * Error response DTO for consistent error handling across the API.
 */
data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String? = null
)

/**
 * Global exception handler for validation errors.
 */
@Produces
@Singleton
@Requires(classes = [ConstraintViolationException::class, ExceptionHandler::class])
class ValidationExceptionHandler : ExceptionHandler<ConstraintViolationException, HttpResponse<ErrorResponse>> {

    private val logger = LoggerFactory.getLogger(ValidationExceptionHandler::class.java)

    override fun handle(request: HttpRequest<*>, exception: ConstraintViolationException): HttpResponse<ErrorResponse> {
        val violations = exception.constraintViolations.joinToString(", ") {
            "${it.propertyPath}: ${it.message}"
        }

        logger.warn("Validation error on ${request.path}: $violations")

        val errorResponse = ErrorResponse(
            status = 400,
            error = "Bad Request",
            message = violations,
            path = request.path
        )

        return HttpResponse.badRequest(errorResponse)
    }
}

/**
 * Global exception handler for illegal argument errors.
 */
@Produces
@Singleton
@Requires(classes = [IllegalArgumentException::class, ExceptionHandler::class])
class IllegalArgumentExceptionHandler : ExceptionHandler<IllegalArgumentException, HttpResponse<ErrorResponse>> {

    private val logger = LoggerFactory.getLogger(IllegalArgumentExceptionHandler::class.java)

    override fun handle(request: HttpRequest<*>, exception: IllegalArgumentException): HttpResponse<ErrorResponse> {
        logger.warn("Invalid argument on ${request.path}: ${exception.message}")

        val errorResponse = ErrorResponse(
            status = 400,
            error = "Bad Request",
            message = exception.message ?: "Invalid argument",
            path = request.path
        )

        return HttpResponse.badRequest(errorResponse)
    }
}

