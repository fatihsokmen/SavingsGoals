package com.github.fatihsokmen.savingsgoals.data

import com.github.fatihsokmen.savingsgoals.data.model.AddIntoSavingsGoalRequestDto
import com.github.fatihsokmen.savingsgoals.data.model.AmountDto
import com.github.fatihsokmen.savingsgoals.data.model.CreateSavingsGoalRequestDto
import com.github.fatihsokmen.savingsgoals.data.model.SavingsGoalDto
import com.github.fatihsokmen.savingsgoals.data.model.TransferIntoGoalResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import javax.inject.Inject

interface SavingsGoalsRepository {
    fun getSavingsGoals(accountUid: String): Flow<List<SavingsGoalDto>>
    suspend fun createSavingsGoal(accountUid: String, name: String): String
    suspend fun addIntoSavingsGoal(
        accountUid: String,
        savingsGoalUid: String,
        transferUid: String,
        amount: BigDecimal
    ): TransferIntoGoalResponseDto
}

class SavingsGoalsRepositoryImpl @Inject constructor(
    private val savingsGoalsApiService: SavingsGoalsApiService
): SavingsGoalsRepository {

    override fun getSavingsGoals(accountUid: String) = flow {
        emit(savingsGoalsApiService.getSavingsGoals(accountUid).savingsGoalList)
    }

    override suspend fun createSavingsGoal(
        accountUid: String, name: String
    ) =
        savingsGoalsApiService.createSavingsGoal(
            accountUid,
            CreateSavingsGoalRequestDto(name, GBP_SYMBOL)
        ).savingsGoalUid

    override suspend fun addIntoSavingsGoal(
        accountUid: String,
        savingsGoalUid: String,
        transferUid: String,
        amount: BigDecimal
    ) =
        savingsGoalsApiService.addIntoSavingsGoal(
            accountUid,
            savingsGoalUid,
            transferUid,
            AddIntoSavingsGoalRequestDto(
                amount = AmountDto(
                    currency = GBP_SYMBOL,
                    minorUnits = amount.movePointRight(2).toLong()
                )
            )
        )

    companion object {
        private const val GBP_SYMBOL = "GBP"
    }
}