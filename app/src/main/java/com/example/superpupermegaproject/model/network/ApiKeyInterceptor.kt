package com.example.superpupermegaproject.model.network.api_responses

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}