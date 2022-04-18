package com.baboci.exchangerate.service

import com.baboci.exchangerate.model.Converter
import java.math.BigDecimal

interface ExchangeRateService {
    fun rate(currencyPair: String): BigDecimal
    fun convert(converter: Converter): BigDecimal
    fun supportedCurrencies(): Map<String, Int>
    fun chart(currencyPair: String): String
}