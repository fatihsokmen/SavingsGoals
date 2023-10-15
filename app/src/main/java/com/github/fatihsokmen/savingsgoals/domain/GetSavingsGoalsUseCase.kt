package com.github.fatihsokmen.savingsgoals.domain

import com.github.fatihsokmen.savingsgoals.core.di.Dispatcher
import com.github.fatihsokmen.savingsgoals.core.di.Dispatchers.IO
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepository
import com.github.fatihsokmen.savingsgoals.domain.model.SavingsGoal
import com.github.fatihsokmen.savingsgoals.domain.model.TotalSaved
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

/**
 * - I will skip mapper injection that inline mapping is done below
 * - One account per user
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GetSavingsGoalsUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val savingsGoalsRepository: SavingsGoalsRepository,
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher
) {

    fun execute() =
        accountsRepository.getAccounts()
            .flatMapLatest { accounts ->
                val account = accounts.first()
                savingsGoalsRepository.getSavingsGoals(account.accountUid)
            }.map {
                it.map { dto ->
                    SavingsGoal(
                        dto.savingsGoalUid,
                        dto.name,
                        TotalSaved(
                            dto.totalSaved.currency,
                            BigDecimal(dto.totalSaved.minorUnits).stripTrailingZeros()
                                .movePointLeft(2)
                        )
                    )
                }
            }
            .flowOn(dispatcher)
}