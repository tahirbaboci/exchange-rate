package com.baboci.exchangerate.infrastructure

import com.baboci.exchangerate.model.ReferenceRate
import org.springframework.stereotype.Repository

@Repository
class InMemoryExchangeRateRepository: ExchangeRateRepository {
    private var referenceRates: MutableMap<String, Int> = mutableMapOf()

    override fun all(): Map<String, Int> {
        return referenceRates
    }

    override fun upsert(referenceRate: ReferenceRate) {
        if(referenceRates.contains(referenceRate.currency)){
            val value = referenceRates[referenceRate.currency] ?: 0
            this.referenceRates[referenceRate.currency] = value + 1
        }else{
            this.referenceRates[referenceRate.currency] = 0
        }
    }

    override fun exists(currency: String): Boolean {
        return referenceRates[currency] != null
    }
    
    override fun empty() = referenceRates.clear()
}