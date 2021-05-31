package com.example.gdsc_app_mobile

import com.example.gdsc_app_mobile.models.ContactModel
import com.example.gdsc_app_mobile.models.LoginModel
import com.example.gdsc_app_mobile.models.TokenModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @GET("v1/contact")
    fun getContacts(@Header("Authorization") authHeader : String) : Call<List<ContactModel>>

    @POST("v1/auth/login")
    fun postLoginEntry(@Body model: LoginModel) : Call<TokenModel>

}