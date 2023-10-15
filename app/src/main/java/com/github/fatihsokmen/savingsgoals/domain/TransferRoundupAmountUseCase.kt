package com.github.fatihsokmen.savingsgoals.domain

import com.github.fatihsokmen.savingsgoals.core.di.Dispatcher
import com.github.fatihsokmen.savingsgoals.core.di.Dispatchers
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

/**
 * Transfers a round-up calculation into a savings goal
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TransferRoundupAmountUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val savingsGoalsRepository: SavingsGoalsRepository,
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher
) {
    fun execute(savingsGoalUid: String, amount: BigDecimal) =
        accountsRepository.getAccounts()
            .flatMapLatest { accounts ->
                val account = accounts.first()
                flow {
                    emit(
                        savingsGoalsRepository.addIntoSavingsGoal(
                            accountUid = account.accountUid,
                            savingsGoalUid = savingsGoalUid,
                            transferUid = UUID.randomUUID().toString(),
                            amount = amount
                        )
                    )
                }
            }.flowOn(dispatcher)
}