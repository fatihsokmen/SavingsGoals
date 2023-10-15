package com.github.fatihsokmen.savingsgoals.data

import com.github.fatihsokmen.savingsgoals.data.model.GetAccountsResponseDto
import retrofit2.http.GET

interface AccountsApiService{

    @GET("api/v2/accounts")
    suspend fun getAccounts(): GetAccountsResponseDto
}