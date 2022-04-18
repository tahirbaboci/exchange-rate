package com.baboci.exchangerate

import com.baboci.exchangerate.error.BadCurrencyRequestError
import com.baboci.exchangerate.error.CurrencyNotFoundError
import com.baboci.exchangerate.error.UnsupportedCurrencyPairError
import com.baboci.exchangerate.model.*
import com.baboci.exchangerate.service.ExchangeRateService
import com.baboci.exchangerate.service.Helper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.http.HttpTimeoutException

class ExchangeRateServiceTests {

    private val exchangeRateService: ExchangeRateService = mockk()

    @Test
    fun whenGetReferenceRate_thenReturnRate() {
        //given
        every { exchangeRateService.rate("EUR/TRY") } returns BigDecimal(15.5)
        //when
        val result = exchangeRateService.rate("EUR/TRY")
        //then
        verify(exactly = 1) { exchangeRateService.rate("EUR/TRY") }
        assertEquals(BigDecimal(15.5), result)
    }
    @Test
    fun whenGetReferenceRate_thenReturnCurrencyNotFoundError() {
        //given
        every { exchangeRateService.rate("TXT/EUR") }.throws(CurrencyNotFoundError(""))
        //then
        assertThrows<CurrencyNotFoundError> { exchangeRateService.rate("TXT/EUR") }
        verify(exactly = 1) { exchangeRateService.rate("TXT/EUR") }
    }

    @Test
    fun whenGetReferenceRate_thenReturnHttpTimeoutException() {
        //given
        every { exchangeRateService.rate("EUR/TRY") }.throws(HttpTimeoutException(""))
        //then
        assertThrows<HttpTimeoutException> { exchangeRateService.rate("EUR/TRY") }
        verify(exactly = 1) { exchangeRateService.rate("EUR/TRY") }
    }

    @Test
    fun whenConvertAmount_thenConvertedAmount() {
        //given
        every { exchangeRateService.convert(Converter(10, "EUR/TRY")) } returns BigDecimal(155.00)
        //when
        val result = exchangeRateService.convert(Converter(10, "EUR/TRY"))
        //then
        verify(exactly = 1) { exchangeRateService.convert(Converter(10, "EUR/TRY")) }
        assertEquals(BigDecimal(155.00), result)
    }

    @Test
    fun whenConvertAmount_thenReturnBadCurrencyError() {
        //given
        every { exchangeRateService.convert(Converter(10, "EUR")) }.throws(BadCurrencyRequestError(""))
        //then
        assertThrows<BadCurrencyRequestError> { exchangeRateService.convert(Converter(10, "EUR")) }
        verify(exactly = 1) { exchangeRateService.convert(Converter(10, "EUR")) }
    }

    @Test
    fun whenConvertAmount_thenReturnCurrencyNotFoundError() {
        //given
        every { exchangeRateService.convert(Converter(10, "EUR/abc")) }.throws(CurrencyNotFoundError(""))
        //then
        assertThrows<CurrencyNotFoundError> { exchangeRateService.convert(Converter(10, "EUR/abc")) }
        verify(exactly = 1) { exchangeRateService.convert(Converter(10, "EUR/abc")) }
    }

    @Test
    fun whenRetrievingSupportedCurrencies_thenReturnAllSupportedCurrencies() {
        val expectedResult = mapOf("EUR" to 0, "TRY" to 0, "USD" to 0)
        //given
        every { exchangeRateService.supportedCurrencies() } returns expectedResult
        //when
        val result = exchangeRateService.supportedCurrencies()
        //then
        verify(exactly = 1) { exchangeRateService.supportedCurrencies() }
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenRequestingPublicChart_thenLinkOfPublicChartFromGoogle() {
        val expectedResult = "www.google.com/search?q=EUR-USD"
        //given
        every { exchangeRateService.chart("EUR/USD") } returns expectedResult
        //when
        val result = exchangeRateService.chart("EUR/USD")
        //then
        verify(exactly = 1) { exchangeRateService.chart("EUR/USD") }
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenRequestingPublicChartWithUnsupportedCurrency_thenReturnUnsupportedCurrencyPairError() {
        //given
        every { exchangeRateService.chart("EUR/asd") } throws UnsupportedCurrencyPairError("")
        //then
        assertThrows<UnsupportedCurrencyPairError> { exchangeRateService.chart("EUR/asd") }
        verify(exactly = 1) { exchangeRateService.chart("EUR/asd") }
    }
    @Test
    fun whenRequestingPublicChartWithWrongCurrencyPair_thenBadCurrencyRequestError() {
        //given
        every { exchangeRateService.chart("asd") } throws BadCurrencyRequestError("")
        //then
        assertThrows<BadCurrencyRequestError> { exchangeRateService.chart("asd") }
        verify(exactly = 1) { exchangeRateService.chart("asd") }
    }

    @Test
    fun whenCalculateCrossRate_thenReturnCorrectCalculation() {
        val expectedResult = BigDecimal(13.39).setScale(2, RoundingMode.HALF_EVEN)
        val result = Helper.calculateCrossRate(
                                ReferenceRate("USD", BigDecimal(1.13)),
                                ReferenceRate("TRY", BigDecimal(15.22)))
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenParseXml_thenReturnListOfReferenceRate() {
        val expectedReferenceRates = listOf(
            ReferenceRate("USD", BigDecimal(1.1348).setScale(2, RoundingMode.HALF_EVEN)),
            ReferenceRate("JPY", BigDecimal(129.14).setScale(2, RoundingMode.HALF_EVEN))
        )
        val result = Helper.parseXmlToReferenceRate(TestData.xmlReferenceRates())
        assertEquals(expectedReferenceRates, result)
    }

    @Test
    fun whenParseCurrencyPair_thenReturnCurrencyPairObject() {
        val resultEuroToOther = Helper.parseCurrencyPair("EUR/TRY")
        assertEquals(EURtoOther::class.java, resultEuroToOther::class.java)
        val resultOtherToOther = Helper.parseCurrencyPair("USD/TRY")
        assertEquals(OtherToOther::class.java, resultOtherToOther::class.java)
        val resultOtherToEuro = Helper.parseCurrencyPair("TRY/EUR")
        assertEquals(OtherToEUR::class.java, resultOtherToEuro::class.java)
        val resultSame = Helper.parseCurrencyPair("TRY/TRY")
        assertEquals(Same::class.java, resultSame::class.java)
    }
}