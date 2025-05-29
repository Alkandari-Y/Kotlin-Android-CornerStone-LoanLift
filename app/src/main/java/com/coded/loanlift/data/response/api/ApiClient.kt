package com.coded.loanlift.data.response.api



import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object ApiClient {

    // Optional: Add auth headers if needed
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer ${getToken()}")
            .build()
        chain.proceed(request)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor) // comment out if you don’t need auth
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8082/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    val kycApi: KYCApi = retrofit.create(KYCApi::class.java)

    // Replace this with your real token-fetching logic
    private fun getToken(): String {
        return "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInVzZXJJZCI6Miwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlzQWN0aXZlIjp0cnVlLCJ0eXBlIjoiYWNjZXNzIiwiaWF0IjoxNzQ4NTM1MDkwLCJleHAiOjE3NDkxMzk4OTB9.K7cOrbvKnDkwoO9GoB3a3qtMod8ZACxZAGI8lyIpky2TsU0B-yIGdTtAjS884XQ8"
    }
}
