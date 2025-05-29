package com.coded.loanlift.providers

import com.coded.loanlift.managers.TokenManager
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val AUTH_SERVICE_PORT = 8081
    private const val BANK_SERVICE_PORT = 8082
    private const val CAMPAIGN_SERVICE_PORT = 8083

    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(context))
            .build()
    }

    fun getAuthServiceProvider(context: Context): AuthServiceProvider {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl(AUTH_SERVICE_PORT))
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthServiceProvider::class.java)
    }

    fun getBankingServiceProvide(context: Context): BankingServiceProvider {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl(BANK_SERVICE_PORT))
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BankingServiceProvider::class.java)
    }

    fun campaignApiService(context: Context): CampaignServiceProvider {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl(CAMPAIGN_SERVICE_PORT))
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CampaignServiceProvider::class.java)
    }

    private fun getBaseUrl(port: Int): String = "http://10.0.2.2:$port/"
}
