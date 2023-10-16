package com.github.fatihsokmen.savingsgoals.data

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.data.model.AccountDto
import com.github.fatihsokmen.savingsgoals.data.model.GetAccountsResponseDto
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AccountsRepositoryImplTest {

    private val accountsApiService: AccountsApiService = mockk()

    @Test
    fun `GIVEN account repository WHEN accounts call made THEN it should fetch from api service`() =
        runTest {
            val accountResponse =
                GetAccountsResponseDto(
                    listOf(
                        AccountDto("account-uid", "default-category")
                    )
                )
            coEvery { accountsApiService.getAccounts() } returns accountResponse
            val subject = AccountsRepositoryImpl(accountsApiService)

            subject.getAccounts().test {
                awaitItem() shouldBeSameSizeAs listOf(accountResponse)
                awaitComplete()
            }
        }
}