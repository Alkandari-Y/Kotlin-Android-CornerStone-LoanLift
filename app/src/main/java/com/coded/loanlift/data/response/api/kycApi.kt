package com.coded.loanlift.data.response.api

import com.coded.loanlift.data.response.KYCRequest
import com.coded.loanlift.data.response.KYCResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface KYCApi {

    @POST("/kyc")
    suspend fun updateKyc(@Body request: KYCRequest): KYCResponse
}