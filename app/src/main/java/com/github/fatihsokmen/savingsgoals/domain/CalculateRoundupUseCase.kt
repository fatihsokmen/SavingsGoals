package com.github.fatihsokmen.savingsgoals.domain

import com.github.fatihsokmen.savingsgoals.core.di.Dispatcher
import com.github.fatihsokmen.savingsgoals.core.di.Dispatchers
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.TransactionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import javax.inject.Inject

/**
 * For given 2 dates (which covers a weeks), transaction amounts are rounded up to nearest pound
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CalculateRoundupUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val transactionsRepository: TransactionsRepository,
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher
) {
    fun execute(start: Instant, end: Instant) =
        accountsRepository.getAccounts()
            .flatMapLatest { accounts ->
                val account = accounts.first()
                transactionsRepository.getFeed(
                    accountUid = account.accountUid,
                    categoryUid = account.defaultCategory,
                    start = start,
                    end = end
                )
            }
            .map { transactions ->
                transactions.fold(BigDecimal.ZERO) { sum, transaction ->
                    val amount = BigDecimal.valueOf(transaction.amount.minorUnits.toDouble())
                        .stripTrailingZeros()
                        .movePointLeft(GBP_MONETARY_UNIT)
                    val delta =
                        amount.round(MathContext(GBP_MONETARY_UNIT, RoundingMode.UP)) - amount

                    sum + delta
                }
            }
            .flowOn(dispatcher)

    companion object {
        private const val GBP_MONETARY_UNIT = 2
    }
}