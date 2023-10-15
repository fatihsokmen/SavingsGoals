package com.github.fatihsokmen.savingsgoals.data

import com.github.fatihsokmen.savingsgoals.data.model.AddIntoSavingsGoalRequestDto
import com.github.fatihsokmen.savingsgoals.data.model.CreateSavingsGoalRequestDto
import com.github.fatihsokmen.savingsgoals.data.model.CreateSavingsGoalResponseDto
import com.github.fatihsokmen.savingsgoals.data.model.GetSavingsGoalsResponseDto
import com.github.fatihsokmen.savingsgoals.data.model.TransferIntoGoalResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface SavingsGoalsApiService {

    @GET("api/v2/account/{accountUid}/savings-goals")
    suspend fun getSavingsGoals(@Path("accountUid") accountUid: String): GetSavingsGoalsResponseDto

    @PUT("api/v2/account/{accountUid}/savings-goals")
    suspend fun createSavingsGoal(
        @Path("accountUid") accountUid: String,
        @Body request: CreateSavingsGoalRequestDto
    ): CreateSavingsGoalResponseDto

    @PUT("api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    suspend fun addIntoSavingsGoal(
        @Path("accountUid") accountUid: String,
        @Path("savingsGoalUid") savingsGoalUid: String,
        @Path("transferUid") transferUid: String,
        @Body request: AddIntoSavingsGoalRequestDto
    ): TransferIntoGoalResponseDto
}