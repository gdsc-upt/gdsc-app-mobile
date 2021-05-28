package com.example.gdsc_app_mobile

import com.example.gdsc_app_mobile.models.ContactModel
import retrofit2.Call
import retrofit2.http.GET

interface UserService {

    @GET("users")
    fun getAllUsers() : Call<List<UserResponse>>

    @GET("api/v1/contact")
    fun getContacts() : Call<List<ContactModel>>
}