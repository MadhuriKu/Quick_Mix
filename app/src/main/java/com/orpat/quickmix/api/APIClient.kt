package com.orpat.quickmix.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orpat.quickmix.BuildConfig
import com.orpat.quickmix.prefs
import com.orpat.quickmix.utility.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private var okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthTokenInterceptor())
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .build()

    private val client: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    private val auth_client: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    private val auth_api_client: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    fun APIinterface(): ApiInterface {
        return client.create<ApiInterface>(ApiInterface::class.java)
    }

    fun ApiAuthInterface(): ApiInterface {
        return auth_client.create<ApiInterface>(ApiInterface::class.java)
    }

    fun ApiAuthTokenInterface(): ApiInterface {
        return auth_api_client.create<ApiInterface>(ApiInterface::class.java)
    }

}

class AuthTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer "+prefs.token)
                .header("platform", "android")
                .header("version", "1.0.0")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}