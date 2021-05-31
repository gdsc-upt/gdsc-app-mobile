package com.example.gdsc_app_mobile

import com.example.gdsc_app_mobile.models.ContactModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("v1/contact")
    fun getContacts() : Call<List<ContactModel>>
}