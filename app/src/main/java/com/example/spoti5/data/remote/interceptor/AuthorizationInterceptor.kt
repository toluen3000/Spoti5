package com.example.spoti5.data.remote.interceptor

import com.example.spoti5.utils.SharePrefUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val sharedPrefer: SharePrefUtils
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = sharedPrefer.saveAccessToken
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}