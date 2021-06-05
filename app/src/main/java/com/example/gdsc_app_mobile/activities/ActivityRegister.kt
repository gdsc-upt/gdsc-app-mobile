package com.example.gdsc_app_mobile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.Toast
import com.example.gdsc_app_mobile.HelperClass.Companion.decodeBase64
import com.example.gdsc_app_mobile.HelperClass.Companion.deserializeTokenInfo
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.models.TokenInfoModel
import com.google.gson.Gson

class ActivityRegister : AppCompatActivity() {

    /*private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirmPassword : EditText*/
    private lateinit var register : Button
    private val TAG = "MyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /*username = findViewById(R.id.register_username_text)
        email = findViewById(R.id.register_email_text)
        password = findViewById(R.id.register_password_text)
        confirmPassword = findViewById(R.id.register_confirm_password_text)*/
        register = findViewById(R.id.register_button)


        register.setOnClickListener{
            val data = Singleton.getToken()?.split(".")?.get(1)
            //Toast.makeText(applicationContext, data.toString(), Toast.LENGTH_LONG).show()
            if (data != null) {
                val decodeValue = data.decodeBase64()
                val tokenInfo = decodeValue.deserializeTokenInfo()
                Toast.makeText(applicationContext, tokenInfo.roles, Toast.LENGTH_LONG).show()
            }

        }
    }
}

