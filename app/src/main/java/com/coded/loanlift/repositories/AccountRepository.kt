package com.coded.loanlift.repositories

import android.content.Context
import com.coded.loanlift.data.response.accounts.AccountCreateRequest
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.providers.RetrofitInstance

object AccountRepository {
    var myAccounts = mutableListOf<AccountDto>()

    suspend fun createAccount(request: AccountCreateRequest, context: Context): Result<AccountDto> {
        return try {
            val service = RetrofitInstance.getBankingServiceProvide(context)
            val response = service.createAccount(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    myAccounts.add(it)
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("Server error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCachedAccounts(): List<AccountDto> = myAccounts

}