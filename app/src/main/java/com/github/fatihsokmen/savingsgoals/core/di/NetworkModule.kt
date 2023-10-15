package com.github.fatihsokmen.savingsgoals.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.github.fatihsokmen.savingsgoals.core.auth.TokenAuthenticator
import com.github.fatihsokmen.savingsgoals.core.auth.TokenHeaderInterceptor
import com.github.fatihsokmen.savingsgoals.core.auth.TokenRefreshInterceptor
import com.github.fatihsokmen.savingsgoals.core.auth.TokenRefreshService
import com.github.fatihsokmen.savingsgoals.core.auth.TokenStore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpCallFactory(
        tokenRefreshServiceProvider: Provider<TokenRefreshService?>,
        tokenStore: TokenStore
    ): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
        )
        .addInterceptor(
            TokenRefreshInterceptor(
                tokenRefreshServiceProvider,
                tokenStore
            )
        )
        .addInterceptor(
            TokenHeaderInterceptor(
                tokenStore
            )
        )
        .authenticator(TokenAuthenticator()) // Not used as sandbox never returns 401
        .build()

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }


    @Provides
    @Singleton
    fun providesTokenStore(dataStore: DataStore<Preferences>) =
        TokenStore(dataStore)

    @Provides
    @Singleton
    fun providesRetrofit(okhttpCallFactory: Call.Factory, networkJson: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api-sandbox.starlingbank.com/")
            .callFactory(okhttpCallFactory)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()

    @Provides
    @Singleton
    fun providesTokenRefreshService(retrofit: Retrofit): TokenRefreshService =
        retrofit.create(TokenRefreshService::class.java)

}
