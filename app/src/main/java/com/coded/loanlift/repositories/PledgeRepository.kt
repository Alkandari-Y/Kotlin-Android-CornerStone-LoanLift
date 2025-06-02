package com.coded.loanlift.repositories

import android.content.Context
import com.coded.loanlift.data.response.pledges.PledgeResultDto
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.providers.RetrofitInstance

object PledgeRepository {
    var userPledges = listOf<PledgeResultDto>()
    private val cachedPledges = mutableListOf<UserPledgeDto>()

    suspend fun getPledgesDetails(context: Context, pledgeId: Long): Result<List<UserPledgeDto>>{
        return try {
            val service = RetrofitInstance.getCampaignApiService(context)
            val response = service.getAllMyPledges()

            if (response.isSuccessful){
                response.body()?.let {
                    cachedPledges.addAll(it)
                    Result.success(it)
                } ?: Result.failure(Exception(response.message()))
            } else {
                Result.failure(Exception("Server Error ${response.code()}"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    fun getCachedPledges(): List<UserPledgeDto> = cachedPledges

    fun getCachedPledgeById(pledgeId: Long): UserPledgeDto? {
        return cachedPledges.find { it.id == pledgeId }
    }

    fun clearCache() {
        cachedPledges.clear()
    }
}