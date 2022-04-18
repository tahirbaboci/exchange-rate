package com.baboci.exchangerate.controller

import com.baboci.exchangerate.service.ExchangeRateService
import com.baboci.exchangerate.model.Converter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/exchange")
class ExchangeRateController(val service: ExchangeRateService) {

    @PostMapping("/reference")
    fun reference(@RequestBody currencyPair: String): ResponseEntity<BigDecimal> {
        return ResponseEntity.status(HttpStatus.OK).body(service.rate(currencyPair))
    }

//    @GetMapping("/reference/{currencyPair}")
//    fun reference(@PathVariable currencyPair: String): ResponseEntity<BigDecimal> {
//        return ResponseEntity.status(HttpStatus.OK).body(service.rate(currencyPair))
//    }

    @PostMapping("/convert")
    fun converter(@RequestBody currency: Converter): ResponseEntity<BigDecimal> {
        return ResponseEntity.status(HttpStatus.OK).body(service.convert(currency))
    }

//    @GetMapping("/convert/{amount}/{currencyPair}")
//    fun converter(@PathVariable amount: Int, @PathVariable currencyPair: String): ResponseEntity<BigDecimal> {
//        return ResponseEntity.status(HttpStatus.OK).body(service.convert(currency))
//    }

    @GetMapping("/supportedCurrencies")
    fun currencies(): ResponseEntity<Map<String, Int>> {
        return ResponseEntity.status(HttpStatus.OK).body(service.supportedCurrencies())
    }

    @PostMapping("/chart")
    fun chart(@RequestBody currencyPair: String): ResponseEntity<String>  {
        return ResponseEntity.status(HttpStatus.OK).body(service.chart(currencyPair))
    }

//    @PostMapping("/chart/{currencyPair}")
//    fun chart(@PathVariable currencyPair: String): ResponseEntity<String>  {
//        return ResponseEntity.status(HttpStatus.OK).body(service.chart(currencyPair))
//    }
}