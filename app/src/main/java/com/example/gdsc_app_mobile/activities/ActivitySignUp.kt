package com.example.gdsc_app_mobile.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.gdsc_app_mobile.R

class ActivitySignUp : AppCompatActivity() {

    private lateinit var logInButton : Button
    private lateinit var registerButton : TextView
    private lateinit var username : EditText
    private lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        logInButton = findViewById(R.id.log_in_button)
        registerButton = findViewById(R.id.activity_sign_up_register)
        username = findViewById(R.id.activity_sign_up_username)
        password = findViewById(R.id.activity_sign_up_password)

        registerButton.setOnClickListener{
            val intent = Intent(this@ActivitySignUp, ActivityRegister::class.java)
            startActivity(intent)
            finish()
        }

        logInButton.setOnClickListener {
            val intent = Intent(this@ActivitySignUp, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupMode()

    }

    private fun setupMode() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("save", 0)
        val state: Boolean = sharedPreferences.getBoolean("dark_mode", false)
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}