package com.baboci.exchangerate.model

sealed interface CurrencyPair

object OtherToEUR: CurrencyPair
object EURtoOther: CurrencyPair
object OtherToOther: CurrencyPair
object Same: CurrencyPair