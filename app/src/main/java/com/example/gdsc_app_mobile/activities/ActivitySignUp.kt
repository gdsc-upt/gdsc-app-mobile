package com.example.gdsc_app_mobile.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.gdsc_app_mobile.services.ApiClient
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.dialogs.DialogCloseApp
import com.example.gdsc_app_mobile.models.LoginModel
import com.example.gdsc_app_mobile.models.TokenModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivitySignUp : AppCompatActivity() {

    private lateinit var logInButton : Button
    private lateinit var registerButton : TextView
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var loginModel: LoginModel

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
        }

        logInButton.setOnClickListener {

            loginModel = LoginModel(username.text.toString(), password.text.toString())
            val loginCall : Call<TokenModel> = ApiClient.getService().postLoginEntry(loginModel)

            loginCall.enqueue(object : Callback<TokenModel>{
                override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {

                    if(response.isSuccessful){
                        val token = response.body()?.token
                        Singleton.setToken(token)

                        /*
                        val contactCall : Call<List<ContactModel>> = ApiClient.getService().
                        getContacts("Bearer $token")

                        contactCall.enqueue(object : Callback<List<ContactModel>> {
                            override fun onResponse(
                                call: Call<List<ContactModel>>,
                                response: Response<List<ContactModel>>
                            ) {
                                if(response.isSuccessful) {
                                    val contacts: List<ContactModel>? = response.body()

                                    if(contacts != null)
                                        if(contacts.isEmpty())
                                            Toast.makeText(this@ActivitySignUp, "Empty list", Toast.LENGTH_SHORT).show()
                                        else
                                            Toast.makeText(this@ActivitySignUp, "Date: ${contacts[0].name}", Toast.LENGTH_LONG).show()
                                    //else
                                    //    Toast.makeText(this@ActivityRegister, "Empty list", Toast.LENGTH_SHORT).show()

                                }
                                else
                                    Toast.makeText(this@ActivitySignUp, "Unsuccessful response", Toast.LENGTH_SHORT).show()

                            }

                            override fun onFailure(call: Call<List<ContactModel>>, t: Throwable) {
                                Toast.makeText(this@ActivitySignUp, "Failed", Toast.LENGTH_SHORT).show()

                            }

                        })
                        */
                    }

                }

                override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
                }

            })

            val intent = Intent(applicationContext, MainActivity::class.java)
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

    override fun onBackPressed() {              //When back pressed, a dialog box will appear with a specific message
        val dialog = DialogCloseApp()
        dialog.show(this.supportFragmentManager, "CloseAppDialog")
    }

}