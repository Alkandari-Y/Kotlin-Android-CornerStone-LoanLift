package com.coded.loanlift.repositories

import android.content.Context
import com.coded.loanlift.data.response.pledges.PledgeResultDto
import com.coded.loanlift.data.response.pledges.PledgeTransactionWithDetails
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.providers.RetrofitInstance

object PledgeTransactionRepository {
    private var pledgeTransactions: MutableMap<Long, List<PledgeTransactionWithDetails>> = mutableMapOf()

    suspend fun fetchPledgeTransactions(context: Context, pledgeId: Long): Result<List<PledgeTransactionWithDetails>> {
        return try {
            val service = RetrofitInstance.getCampaignApiService(context)
            val response = service.getPledgeDetailedTransactions(pledgeId)

            if (response.isSuccessful) {
                response.body()?.let {
                    pledgeTransactions[pledgeId] = it
                    Result.success(it)
                } ?: Result.failure(Exception(response.message()))
            } else {
                Result.failure(Exception("Server Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun findTransactionsByPledgeId(pledgeId: Long): List<PledgeTransactionWithDetails>? {
        return pledgeTransactions[pledgeId]
    }

    fun clearCache() {
        pledgeTransactions.clear()
    }
}