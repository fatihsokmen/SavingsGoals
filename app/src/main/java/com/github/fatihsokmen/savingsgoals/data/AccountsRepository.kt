package com.github.fatihsokmen.savingsgoals.data

import com.github.fatihsokmen.savingsgoals.data.model.AccountDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface AccountsRepository {
    fun getAccounts(): Flow<List<AccountDto>>
}

/**
 * In this coding test, accounts are fetched from api every time account info is needed.
 * Alternatively we can create a composite repository using remote datasource to fetch and
 * local repository to cache account locally unless account information is invalidated
 * or user signs out
 *
 * Ex:
 * AccountsRepositoryImpl(
 *      private val accountsRemoteDataSource: AccountsRemoteDataSource,
 *      private val accountsLocalDataSource: AccountsLocalDataSource
 * )
 *
 */
class AccountsRepositoryImpl @Inject constructor(
    private val accountsApiService: AccountsApiService
) : AccountsRepository {

    override fun getAccounts() = flow {
        emit(accountsApiService.getAccounts().accounts)
    }
}