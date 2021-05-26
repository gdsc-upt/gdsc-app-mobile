package com.example.gdsc_app_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ActivityRegister : AppCompatActivity() {

    private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirmPassword : EditText
    private lateinit var register : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        username = findViewById(R.id.register_username_text)
        email = findViewById(R.id.register_email_text)
        password = findViewById(R.id.register_password_text)
        confirmPassword = findViewById(R.id.register_confirm_password_text)
        register = findViewById(R.id.register_button)

        register.setOnClickListener{

        }
    }


}