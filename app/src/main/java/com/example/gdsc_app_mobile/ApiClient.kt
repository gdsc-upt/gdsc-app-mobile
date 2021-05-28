package com.example.gdsc_app_mobile

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        fun getRetrofit() : Retrofit {
            val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl("http://dev.api.gdscupt.tech/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }

        fun getUserService() : UserService {
            val userService : UserService = getRetrofit().create(UserService::class.java)

            return userService
        }
    }
}