package com.example.gdsc_app_mobile

object Singleton {

    private var token: String? = null

    fun getToken(): String? {
        return "Bearer $token"
    }

    fun setToken(tok: String?) {
        token = tok
    }
}