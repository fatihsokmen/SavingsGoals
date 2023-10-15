package com.github.fatihsokmen.savingsgoals.core.auth

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenHeaderInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().signedRequest())
    }

    private fun Request.signedRequest(): Request {
        return newBuilder()
            .header("Authorization", "Bearer ${tokenStore.getAccessToken()}")
            .build()
    }
}