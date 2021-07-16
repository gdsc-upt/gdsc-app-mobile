package com.example.gdsc_app_mobile.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private fun getRetrofit(): Retrofit {
            val okhttpClient: OkHttpClient = OkHttpClient().newBuilder().build()
            return Retrofit.Builder()
                .baseUrl("https://api.gdscupt.tech/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient)
                .build()
        }

        fun getService(): ApiService {
            return getRetrofit().create(ApiService::class.java)
        }
    }
}