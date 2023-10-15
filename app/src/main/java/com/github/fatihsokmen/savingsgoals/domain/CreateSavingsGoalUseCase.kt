package com.github.fatihsokmen.savingsgoals.domain

import com.github.fatihsokmen.savingsgoals.core.di.Dispatcher
import com.github.fatihsokmen.savingsgoals.core.di.Dispatchers.IO
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created a savings goal for an account
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CreateSavingsGoalUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val savingsGoalsRepository: SavingsGoalsRepository,
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher
) {

    /**
     * I am assuming below one user can have one account for simplicity
     * USER <-> ACCOUNT (1-to-1)
     */
    fun execute(name: String) =
        accountsRepository.getAccounts()
            .flatMapLatest { accounts ->
                val account = accounts.first()
                flow {
                    emit(savingsGoalsRepository.createSavingsGoal(account.accountUid, name))
                }
            }
            .flowOn(dispatcher)
}