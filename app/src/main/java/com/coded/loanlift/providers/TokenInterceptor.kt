package com.coded.loanlift.providers

import android.content.Context
import android.util.Log
import com.coded.loanlift.managers.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var token = TokenManager.getToken(context)?.access

        if (TokenManager.isAccessTokenExpired(context)) {
            Log.d("TokenInterceptor", "Token expired, attempting refresh")
            runBlocking {
                val refreshed = TokenManager.refreshToken(context)
                token = refreshed?.access
            }
        }

        val request = chain.request().newBuilder().apply {
            token?.let { addHeader("Authorization", "Bearer $it") }
        }.build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            Log.w("TokenInterceptor", "Unauthorized – clearing tokens")
            TokenManager.clearToken(context)
        }

        return response
    }
}
