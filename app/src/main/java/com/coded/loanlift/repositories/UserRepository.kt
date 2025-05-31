package com.coded.loanlift.repositories

import com.coded.loanlift.data.response.auth.ValidateTokenResponse
import com.coded.loanlift.data.response.kyc.KYCResponse
import android.content.Context
import com.coded.loanlift.providers.RetrofitInstance

object UserRepository {
    var userInfo: ValidateTokenResponse? = null
    var kyc: KYCResponse? = null

    suspend fun loadUserInfo(context: Context) {
        val response = RetrofitInstance.getAuthServiceProvider(context).getUserDetails()
        if (response.isSuccessful) {
            userInfo = response.body()
        } else {
            userInfo = null
        }
    }
}