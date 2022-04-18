package com.baboci.exchangerate.service

import com.baboci.exchangerate.error.BadCurrencyRequestError
import com.baboci.exchangerate.error.CurrencyNotFoundError
import com.baboci.exchangerate.error.RequestedSameCurrencyError
import com.baboci.exchangerate.error.UnsupportedCurrencyPairError
import com.baboci.exchangerate.infrastructure.ExchangeRateRepository
import com.baboci.exchangerate.model.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class ExchangeRateServiceImpl(private val client: EcbClient, private val exchangeRateRepository: ExchangeRateRepository):
    ExchangeRateService {

    @Value("\${redirect.url}")
    lateinit var redirectUrl: String

    override fun rate(currencyPair: String): BigDecimal {
        validate(currencyPair)
        val response = client.requestReferenceRates()
        val parsedXml = Helper.parseXmlToReferenceRate(response.body())
        return findRate(currencyPair, Helper.parseCurrencyPair(currencyPair), parsedXml)
    }

    override fun convert(converter: Converter): BigDecimal {
        validate(converter.currency)
        val response = client.requestReferenceRates()
        val parsedXml = Helper.parseXmlToReferenceRate(response.body())
        val rate = findRate(converter.currency, Helper.parseCurrencyPair(converter.currency), parsedXml)
        return rate.multiply(converter.amount.toBigDecimal()).setScale(2, RoundingMode.HALF_EVEN)
    }

    override fun supportedCurrencies(): Map<String, Int> {
        initializeDatabase()
        return exchangeRateRepository.all()
    }

    override fun chart(currencyPair: String): String {
        validate(currencyPair)
        initializeDatabase()
        val curr = currencyPair.uppercase().split('/')
        curr.map {
            if(!exchangeRateRepository.exists(it))
                throw UnsupportedCurrencyPairError("currency you requested is not supported")
        }
        return "$redirectUrl${curr.first()}-${curr.last()}"
    }


    private fun findRate(request: String, currencyPair: CurrencyPair, referenceRate: List<ReferenceRate>): BigDecimal {
        val curr = request.uppercase().split('/')
        return when (currencyPair) {
            is OtherToOther -> {
                val base = referenceRate
                    .find {
                        it.currency == curr.first()
                    }?: throw CurrencyNotFoundError("base currency you are searching is not found.")
                val quote = referenceRate
                    .find {
                        it.currency == curr.last()
                    }?: throw CurrencyNotFoundError("quote currency you are searching is not found.")
                exchangeRateRepository.upsert(quote)
                Helper.calculateCrossRate(base, quote)
            }
            is EURtoOther -> {
                val quote = referenceRate
                    .find {
                        it.currency == curr.last()
                    }?: throw CurrencyNotFoundError("quote currency you are searching is not found.")
                exchangeRateRepository.upsert(quote)
                quote.rate
            }
            is OtherToEUR -> {
                val base = referenceRate
                    .find {
                        it.currency == curr.first()
                    }?: throw CurrencyNotFoundError("base currency you are searching is not found.")
                exchangeRateRepository.upsert(ReferenceRate("EUR", BigDecimal(1.0)))
                BigDecimal(1).divide(base.rate, 2, RoundingMode.HALF_EVEN)
            }
            is Same -> {
                throw RequestedSameCurrencyError("You have requested rate for the same currency")
            }
        }
    }

    private fun validate(currencyPair: String) {
        val curr = currencyPair.uppercase().split('/')
        if (curr.size < 2)
            throw BadCurrencyRequestError("Bad request: $currencyPair, please give currency pair like this: EUR/TRY")
        else if(curr.first().length != 3 || curr.last().length != 3)
            throw BadCurrencyRequestError("Bad request: $currencyPair, base and quote should be 3 characters. please give currency pair like this: EUR/TRY")
    }

    private fun initializeDatabase() {
        val response = client.requestReferenceRates()
        if(Helper.parseXmlToReferenceRate(response.body()).size + 1 != exchangeRateRepository.all().size){
            Helper.parseXmlToReferenceRate(response.body()).map { exchangeRateRepository.upsert(it) }
            // Euro also is supported in this case, but it is not in the list, adding it to the database
            exchangeRateRepository.upsert(ReferenceRate("EUR", BigDecimal(1.0)))
        }
    }
}

object Helper {
    fun parseCurrencyPair(currencyPair: String): CurrencyPair {
        val curr = currencyPair.uppercase().split('/')
        return if(curr.first() == curr.last())
            Same
        else if(curr.first() == "EUR")
            EURtoOther
        else if(curr.last() == "EUR")
            OtherToEUR
        else
            OtherToOther
    }

    fun parseXmlToReferenceRate(xmlToParse: String): List<ReferenceRate> {
        val doc2: Document = Jsoup.parse(xmlToParse, "", Parser.xmlParser())
        return doc2.getElementsByAttribute("currency")
            .map { ReferenceRate(it.attr("currency"), BigDecimal(it.attr("rate")).setScale(2, RoundingMode.HALF_EVEN)) }
    }

    fun calculateCrossRate(base: ReferenceRate, quote: ReferenceRate): BigDecimal {
        val swapSecondCurrencyRate = BigDecimal(1).divide(base.rate, 2, RoundingMode.HALF_EVEN)
        return quote.rate.multiply(swapSecondCurrencyRate).setScale(2, RoundingMode.HALF_EVEN)
    }
}