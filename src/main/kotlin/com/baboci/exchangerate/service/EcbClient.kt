package com.baboci.exchangerate.service

import org.springframework.beans.factory.annotation.Value
import java.time.Duration
import java.time.temporal.ChronoUnit
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class EcbClient {

    @Value("\${ecb.url}")
    lateinit var url: String
    private val timeout = Duration.of(1000, ChronoUnit.MILLIS)

    private val client: HttpClient = HttpClient.newBuilder().build()

    fun requestReferenceRates():HttpResponse<String> {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(timeout)
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}