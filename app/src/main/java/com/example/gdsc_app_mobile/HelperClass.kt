package com.example.gdsc_app_mobile

import android.util.Base64
import com.example.gdsc_app_mobile.models.TokenInfoModel
import com.google.gson.Gson

class HelperClass {
    companion object {
        fun String.deserializeTokenInfo(): TokenInfoModel {
            val gson = Gson()
            val tokenInfo = gson.fromJson(this, TokenInfoModel::class.java)
            Singleton.setTokenInfo(tokenInfo)
            return tokenInfo
        }
        fun String.decodeBase64(): String {
            return Base64.decode(this, Base64.DEFAULT).decodeToString()
        }
    }
}
