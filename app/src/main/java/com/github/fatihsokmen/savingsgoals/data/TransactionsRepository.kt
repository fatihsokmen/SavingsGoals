package com.github.fatihsokmen.savingsgoals.data

import com.github.fatihsokmen.savingsgoals.data.model.TransactionDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import javax.inject.Inject

/**
 * Coding example is slightly a simple app therefore
 * this repository lives in the same module with other classes
 */

interface TransactionsRepository {
    fun getFeed(
        accountUid: String,
        categoryUid: String,
        start: Instant,
        end: Instant
    ): Flow<List<TransactionDto>>
}

class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsApiSource: TransactionsApiService
) : TransactionsRepository {

    override fun getFeed(
        accountUid: String,
        categoryUid: String,
        start: Instant,
        end: Instant
    ) =
        flow {
            emit(
                transactionsApiSource.geTransactionsBetween(
                    accountUid,
                    categoryUid,
                    start.toString(),
                    end.toString()
                ).feedItems
            )
        }
}