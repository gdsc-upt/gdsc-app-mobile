package com.example.gdsc_app_mobile.services

import com.example.gdsc_app_mobile.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @GET("v1/contact")
    fun getAllContacts(@Header("Authorization") authHeader : String) : Call<List<ContactModel>>

    @GET("v1/faqs")
    fun getAllFaqs(): Call<List<FaqModel>>

    @POST("v1/auth/login")
    fun postLoginEntry(@Body model: LoginModel) : Call<TokenModel>

    @GET("v1/events")
    fun getAllEvents(): Call<List<EventModel>>

}