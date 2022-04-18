package com.baboci.exchangerate.error

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class CurrencyNotFoundError(message: String): RuntimeException(message)
class UnsupportedCurrencyPairError(message: String): RuntimeException(message)
class BadCurrencyRequestError(message: String): RuntimeException(message)
class SupportedCurrenciesNotFoundError(message: String): RuntimeException(message)
class RequestedSameCurrencyError(message: String): RuntimeException(message)

@JsonInclude(content = JsonInclude.Include.NON_NULL)
data class ApiError(
    val errorCode: HttpStatus,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    val timestamp: LocalDateTime? = LocalDateTime.now(),
    val errorMessage: String?,
    val detail: String? = ""
)