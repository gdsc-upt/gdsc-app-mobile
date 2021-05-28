package com.example.gdsc_app_mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.gdsc_app_mobile.ApiClient
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.UserResponse
import com.example.gdsc_app_mobile.models.ContactModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityRegister : AppCompatActivity() {

    /*private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirmPassword : EditText*/
    private lateinit var register : Button
    private lateinit var loginMe : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /*username = findViewById(R.id.register_username_text)
        email = findViewById(R.id.register_email_text)
        password = findViewById(R.id.register_password_text)
        confirmPassword = findViewById(R.id.register_confirm_password_text)*/
        register = findViewById(R.id.register_button)
        loginMe = findViewById(R.id.back_to_login)

        loginMe.setOnClickListener {
            val intent = Intent(this@ActivityRegister, ActivitySignUp::class.java)
            startActivity(intent)
        }

        register.setOnClickListener{

            val contactCall : Call<List<ContactModel>> = ApiClient.getUserService().getContacts()

            contactCall.enqueue(object : Callback<List<ContactModel>> {
                override fun onResponse(
                    call: Call<List<ContactModel>>,
                    response: Response<List<ContactModel>>
                ) {
                    if(response.isSuccessful) {
                        //Toast.makeText(this@ActivityRegister, "Successful response", Toast.LENGTH_SHORT).show()
                        val contacts: List<ContactModel>? = response.body()

                        if(contacts != null)
                            Toast.makeText(this@ActivityRegister, "Date: ${contacts[0].created}", Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(this@ActivityRegister, "Unsuccessful response", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<List<ContactModel>>, t: Throwable) {
                    Toast.makeText(this@ActivityRegister, "Failed", Toast.LENGTH_SHORT).show()

                }

            })
            /*val userCall: Call<List<UserResponse>> = ApiClient.getUserService().getAllUsers()

            userCall.enqueue(object : Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    if (response.isSuccessful) {

                        val users : List<UserResponse>? = response.body()
                        if(users == null)
                            Toast.makeText(
                                this@ActivityRegister,
                                "There are no users",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            Toast.makeText(
                                this@ActivityRegister,
                                "There are ${users.size} users",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                    else
                        Toast.makeText(this@ActivityRegister, "Unsuccessful response", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    Toast.makeText(this@ActivityRegister, "Failure", Toast.LENGTH_SHORT).show()
                }

            })*/
        }
    }


}