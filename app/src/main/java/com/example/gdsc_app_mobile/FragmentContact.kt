package com.example.gdsc_app_mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception

class FragmentContact : Fragment() {

    lateinit var subjectEditText: EditText
    lateinit var messageEditText: EditText
    lateinit var nameEditText: EditText
    lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        nameEditText = view.findViewById(R.id.nameEditText)
        subjectEditText = view.findViewById(R.id.subjectEditText)
        messageEditText = view.findViewById(R.id.messageEditText)
        submitButton = view.findViewById(R.id.submitMessageButton)
        submitButton.setOnClickListener{
            var recipient = "dsc.upt@gmail.com"

            //get input from Edit Texts
            var name = nameEditText.text.toString()
            var subject = subjectEditText.text.toString()
            var message = messageEditText.text.toString() +
                    "\nSent by $name"

            sendEmail(recipient, subject, message)
        }

        return view
    }

    private fun sendEmail(recipient:String, subject:String, message: String){
        val mIntent = Intent(Intent.ACTION_SEND)

        mIntent.data= Uri.parse("mailto")
        mIntent.type = "text/plain"

        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        try{
            startActivity(Intent.createChooser(mIntent, "Choose email client"))
        }
        catch(e:Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }
}