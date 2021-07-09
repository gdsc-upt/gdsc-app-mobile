package com.example.gdsc_app_mobile.services

import com.example.gdsc_app_mobile.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ApiService {

    @GET("v1/faqs")
    fun getAllFaqs(): Call<List<FaqModel>>

    @POST("v1/faqs")
    fun postFaq(@Header("Authorization") authHeader : String, @Body model: FaqPostModel) : Call<FaqModel>

    @DELETE("v1/faqs/{id}")
    fun deleteFaq(@Header("Authorization") authHeader : String,@Path("id") id: String?): Call<FaqModel>

    @POST("v1/auth/login")
    fun postLoginEntry(@Body model: LoginModel) : Call<TokenModel>

    @POST("v1/contact")
    fun postContact(@Body model: ContactPostModel): Call<ContactModel>

    @POST("v1/events")
    fun postEvent(@Header("Authorization") authHeader : String,@Body model: EventModel) : Call<EventModel>

    @GET("v1/events")
    fun getAllEvents(): Call<List<EventModel>>

    @POST("api/v1/files")
    fun postFile(@Body file : File) : Call<FileModel>

    @Multipart
    @POST("api/v1/files")
    fun uploadPhoto(
        @Part("description") description : RequestBody,
        @Part photo : MultipartBody.Part
    );


}