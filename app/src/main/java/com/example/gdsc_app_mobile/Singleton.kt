package com.example.gdsc_app_mobile

import com.example.gdsc_app_mobile.HelperClass.Companion.decodeBase64
import com.example.gdsc_app_mobile.HelperClass.Companion.deserializeTokenInfo
import com.example.gdsc_app_mobile.models.TokenInfoModel

object Singleton {

    private var token: String? = null
    private var tokenInfo: TokenInfoModel? = null

    fun setTokenInfo(tok: TokenInfoModel?) {
        tokenInfo = tok
    }
    fun getTokenInfo(): TokenInfoModel? {
        return tokenInfo
    }

    fun getToken(): String? {
        return token
    }

    fun getTokenForAuthentication(): String? {
        if(token == null)
            return null
        return "Bearer $token"
    }

    fun setToken(tok: String?) {
        token = tok
        if(token != null)
            addTokenInfo(tok)

    }
    private fun addTokenInfo(tok: String?) {
        val data = tok?.split(".")?.get(1)?.decodeBase64()
        setTokenInfo(data!!.deserializeTokenInfo())
    }
}