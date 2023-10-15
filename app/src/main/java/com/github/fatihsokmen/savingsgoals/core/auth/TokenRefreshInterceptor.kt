package com.github.fatihsokmen.savingsgoals.core.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Provider

/**
 * Okhttp Authenticator does not work with 403 which sandbox api responds by default
 * so I am going to use this interceptor to handle 403.
 */

class TokenRefreshInterceptor(
    private val tokenRefreshService: Provider<TokenRefreshService?>,
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val checkedResponse = if (response.code == HTTP_FORBIDDEN) {
            response.close()
            val newAccessToken = runBlocking {
                tokenRefreshService.get()?.refreshAccessToken()?.accessToken.orEmpty()
            }
            tokenStore.putAccessToken(newAccessToken)

            chain.proceed(request)
        } else {
            response
        }
        return checkedResponse
    }

    companion object {
        private const val HTTP_FORBIDDEN = 403
    }
}
