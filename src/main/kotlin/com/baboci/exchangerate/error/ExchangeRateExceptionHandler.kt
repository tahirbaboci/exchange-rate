package com.baboci.exchangerate.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.http.HttpTimeoutException


@ControllerAdvice
class ExchangeRateExceptionHandler {

    private val log = LoggerFactory.getLogger(ExchangeRateExceptionHandler::class.java)

    @ExceptionHandler(CurrencyNotFoundError::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleCurrencyNotFoundError(ex: CurrencyNotFoundError): ApiError {
        log.error(ex.message)
        return ApiError(
            errorCode = HttpStatus.NOT_FOUND,
            errorMessage = ex.message
        )
    }

    @ExceptionHandler(UnsupportedCurrencyPairError::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleUnsupportedCurrencyPairError(ex: UnsupportedCurrencyPairError): ApiError {
        log.error(ex.message)
        return ApiError(
            errorCode = HttpStatus.BAD_REQUEST,
            errorMessage = ex.message
        )
    }

    @ExceptionHandler(BadCurrencyRequestError::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBadCurrencyRequestError(ex: BadCurrencyRequestError): ApiError {
        log.error(ex.message)
        return ApiError(
            errorCode = HttpStatus.BAD_REQUEST,
            errorMessage = ex.message
        )
    }

    @ExceptionHandler(SupportedCurrenciesNotFoundError::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleSupportedCurrenciesNotFound(ex: SupportedCurrenciesNotFoundError): ApiError {
        log.error(ex.message)
        return ApiError(
            errorCode = HttpStatus.NOT_FOUND,
            errorMessage = ex.message
        )
    }

    @ExceptionHandler(RequestedSameCurrencyError::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleRequestedSameCurrencyError(ex: RequestedSameCurrencyError): ApiError {
        log.error(ex.message)
        return ApiError(
            errorCode = HttpStatus.BAD_REQUEST,
            errorMessage = ex.message
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleInternalError(ex: Exception): ApiError {
        log.error(ex.message)
        return ApiError(
            errorCode = HttpStatus.INTERNAL_SERVER_ERROR,
            errorMessage = ex.message
        )
    }

    @ExceptionHandler(HttpTimeoutException::class)
    @ResponseStatus(code = HttpStatus.REQUEST_TIMEOUT)
    @ResponseBody
    fun handleHttpTimeoutException(ex: HttpTimeoutException): ApiError {
        log.error(ex.message)
        return ApiError(
            errorCode = HttpStatus.REQUEST_TIMEOUT,
            errorMessage = ex.message
        )
    }
}