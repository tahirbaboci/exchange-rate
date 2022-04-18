package com.baboci.exchangerate

import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeRateIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @LocalServerPort
    var randomServerPort: Int = 0

    @Before
    fun setUp() {
        testRestTemplate = TestRestTemplate()
    }

    @Test
    fun whenRequestingReferenceRate_ReturnRate() {
        val baseUrl = "http://localhost:" + randomServerPort.toString() + "/exchange/reference"
        val uri = URI(baseUrl)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity("TRY/EUR", headers)
        val result = testRestTemplate
            .postForEntity(uri, request, BigDecimal::class.java)
        Assert.assertNotNull(result)
        Assert.assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun whenRequestingReferenceRate_ReturnCurrencyNotFoundError() {
        val baseUrl = "http://localhost:" + randomServerPort.toString() + "/exchange/reference"
        val uri = URI(baseUrl)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity("abc/EUR", headers)
        val result = testRestTemplate
            .postForEntity(uri, request, String::class.java)
        Assert.assertNotNull(result)
        Assert.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        Assert.assertEquals(true, result.body?.contains("currency you are searching is not found."))
    }

    @Test
    fun whenRequestingConvert_ReturnConvertedAmount() {
        val baseUrl = "http://localhost:" + randomServerPort.toString() + "/exchange/convert"
        val uri = URI(baseUrl)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(TestData.convertTest(), headers)
        val result = testRestTemplate
            .postForEntity(uri, request, BigDecimal::class.java)
        Assert.assertNotNull(result)
        Assert.assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun whenRequestingListOfSupportedCurr_ReturnAllCurrenciesWithRequestedCount() {
        val baseUrl = "http://localhost:" + randomServerPort.toString() + "/exchange/supportedCurrencies"
        val uri = URI(baseUrl)
        val result = testRestTemplate
            .getForEntity(uri, Map::class.java)
        Assert.assertNotNull(result)
        Assert.assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun whenRequestingForChart_ReturnPublicLinkToChart() {
        val baseUrl = "http://localhost:" + randomServerPort.toString() + "/exchange/chart"
        val uri = URI(baseUrl)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity("TRY/EUR", headers)
        val result = testRestTemplate
            .postForEntity(uri, request, String::class.java)
        Assert.assertNotNull(result)
        Assert.assertTrue(result.body.toString().contains("google"))
        Assert.assertEquals(HttpStatus.OK, result.statusCode)
    }
}