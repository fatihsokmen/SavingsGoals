package com.github.fatihsokmen.savingsgoals.data.di

import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.AccountsRepositoryImpl
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepository
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepositoryImpl
import com.github.fatihsokmen.savingsgoals.data.TransactionsRepository
import com.github.fatihsokmen.savingsgoals.data.TransactionsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsAccountsRepository(
        accountsRepository: AccountsRepositoryImpl
    ): AccountsRepository

    @Binds
    fun bindsSavingsGoalRepository(
        goalsRepository: SavingsGoalsRepositoryImpl
    ): SavingsGoalsRepository

    @Binds
    fun bindsTransactionsRepository(
        transactionsRepository: TransactionsRepositoryImpl
    ): TransactionsRepository
}
