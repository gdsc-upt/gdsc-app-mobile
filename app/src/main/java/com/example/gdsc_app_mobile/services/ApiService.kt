package com.example.gdsc_app_mobile.services

import com.example.gdsc_app_mobile.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("v1/faqs")
    fun getAllFaqs(): Call<List<FaqModel>>

    @GET("v1/teams")
    fun getTeams(): Call<List<TeamsModel>>

    @GET("v1/members")
    fun getMembers(): Call<List<MemberModel>>

    @POST("v1/faqs")
    fun postFaq(@Header("Authorization") authHeader : String, @Body model: FaqPostModel) : Call<FaqModel>

    @DELETE("v1/faqs/{id}")
    fun deleteFaq(@Header("Authorization") authHeader : String,@Path("id") id: String?): Call<FaqModel>

    @POST("v1/auth/login")
    fun postLoginEntry(@Body model: LoginModel) : Call<TokenModel>

    @POST("v1/contact")
    fun postContact(@Body model: ContactPostModel): Call<ContactModel>

    @POST("v1/teams")
    fun postTeam(@Header("Authorization") authHeader : String, @Body model: TeamsPostModel): Call<TeamsModel>

}