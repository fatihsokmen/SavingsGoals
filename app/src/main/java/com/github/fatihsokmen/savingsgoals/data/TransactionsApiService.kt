package com.github.fatihsokmen.savingsgoals.data

import com.github.fatihsokmen.savingsgoals.data.model.GetTransactionsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionsApiService {

    @GET("api/v2/feed/account/{accountUid}/category/{categoryUid}/transactions-between")
    suspend fun geTransactionsBetween(
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") categoryUid: String,
        @Query("minTransactionTimestamp") start: String,
        @Query("maxTransactionTimestamp") end: String
    ): GetTransactionsResponseDto
}