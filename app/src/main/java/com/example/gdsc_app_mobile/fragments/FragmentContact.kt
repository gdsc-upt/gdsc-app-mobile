package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.dialogs.DialogFragmentContactMessage
import com.example.gdsc_app_mobile.interfaces.ISelectedDataContact
import com.example.gdsc_app_mobile.models.ContactModel
import com.example.gdsc_app_mobile.models.ContactPostModel
import com.example.gdsc_app_mobile.services.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.net.Uri
import android.widget.*
import com.example.gdsc_app_mobile.HelperClass

class FragmentContact : Fragment(), ISelectedDataContact {

    private lateinit var openDialog: FloatingActionButton
    private lateinit var linkedinButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        val toolbar = (activity as MainActivity).toolbar
        HelperClass.setToolbarStyle(context, toolbar, "contact")

        linkedinButton = view.findViewById(R.id.contact_linkedin_button)

        openDialog = view.findViewById(R.id.contact_floating_button)
        openDialog.setOnClickListener {
            openMessageDialog()
        }

        linkedinButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/company/gdsc-upt/")
                )
            )
        }
        return view
    }

    private fun openMessageDialog(){
        val dialog = DialogFragmentContactMessage()
        dialog.addListener(this)
        dialog.show(requireActivity().supportFragmentManager, "MessageDialog")
    }

    private fun createPost(_name: String, _email: String, _subject: String, _message: String) {
        val model = ContactPostModel(
            name = _name,
            email = _email,
            subject = _subject,
            text = _message
        )
        val contactCall : Call<ContactModel> = ApiClient.getService().postContact(model)

        contactCall.enqueue(object : Callback<ContactModel> {
            override fun onResponse(call: Call<ContactModel>, response: Response<ContactModel>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(), resources.getString(R.string.contact_successfully_posted), Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ContactModel>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSelectedData(name: String, email: String, subject: String, message: String) {
        createPost(name, email, subject, message)
    }


}