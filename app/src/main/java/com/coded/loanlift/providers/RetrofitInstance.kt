package com.coded.loanlift.providers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val AUTH_SERVICE_PORT = 8081
    private const val BANK_SERVICE_PORT = 8082
    private const val CAMPAIGN_SERVICE_PORT = 8083


    val authApiService: AuthServiceProvider by lazy {
        Retrofit.Builder()
            .baseUrl(
                getBaseUrl(port = AUTH_SERVICE_PORT)
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthServiceProvider::class.java)
    }


    val bankingApiService: BankingServiceProvider by lazy {
        Retrofit.Builder()
            .baseUrl(
                getBaseUrl(port = BANK_SERVICE_PORT)
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BankingServiceProvider::class.java)
    }

    val campaignApiService: CampaignServiceProvider by lazy {
        Retrofit.Builder()
            .baseUrl(
                getBaseUrl(port = CAMPAIGN_SERVICE_PORT)
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CampaignServiceProvider::class.java)
    }


    private fun getBaseUrl(port: Int): String =  "http://10.0.2.2:$port/api/v1"
}