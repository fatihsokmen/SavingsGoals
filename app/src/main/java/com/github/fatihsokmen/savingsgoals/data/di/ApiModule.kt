package com.github.fatihsokmen.savingsgoals.data.di

import com.github.fatihsokmen.savingsgoals.data.AccountsApiService
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsApiService
import com.github.fatihsokmen.savingsgoals.data.TransactionsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesSavingsGoalsApi(retrofit: Retrofit): SavingsGoalsApiService =
        retrofit.create(SavingsGoalsApiService::class.java)

    @Provides
    @Singleton
    fun providesAccountsApi(retrofit: Retrofit): AccountsApiService =
        retrofit.create(AccountsApiService::class.java)

    @Provides
    @Singleton
    fun providesTransactionsApi(retrofit: Retrofit): TransactionsApiService =
        retrofit.create(TransactionsApiService::class.java)
}