package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.dialogs.DialogFragmentAddEvent
import com.example.gdsc_app_mobile.interfaces.ISelectedEvent
import com.example.gdsc_app_mobile.models.EventModel

class FragmentHomePage: Fragment(), ISelectedEvent{

    lateinit var add: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        add = view.findViewById(R.id.add_event_button)

        add.setOnClickListener{
            openMessageDialog()
        }

        return view
    }

    private fun openMessageDialog(){
        val dialog = DialogFragmentAddEvent()
        dialog.addListener(this)
        dialog.show(requireActivity().supportFragmentManager, "MessageDialog")
    }

    override fun onSelectedEvent(title: String, description: String, imageUri: String) {
        createPost(title, description, imageUri)
    }

    override fun createPost(title: String, description: String, imageUri: String) {
        TODO("Not yet implemented")
    }

}