package com.baboci.exchangerate.infrastructure

import com.baboci.exchangerate.model.ReferenceRate

interface ExchangeRateRepository {
    fun all(): Map<String, Int>
    fun upsert(referenceRate: ReferenceRate)
    fun exists(currency: String): Boolean
    fun empty()
}