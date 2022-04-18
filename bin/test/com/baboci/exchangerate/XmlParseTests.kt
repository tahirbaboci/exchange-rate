package com.baboci.exchangerate

import com.baboci.exchangerate.model.ReferenceRate
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.jsoup.parser.Parser
import java.math.BigDecimal
import java.math.RoundingMode


class XmlParseTests {

    @Test
    fun testXMLParse() {
        val doc2: Document = Jsoup.parse(TestData.xmlReferenceRates(), "", Parser.xmlParser())
        val referenceRates: List<ReferenceRate> = doc2.getElementsByAttribute("currency").map { ReferenceRate(it.attr("currency"), BigDecimal(it.attr("rate")).setScale(2, RoundingMode.HALF_EVEN)) }
        assertEquals(2, referenceRates.size)
    }
}