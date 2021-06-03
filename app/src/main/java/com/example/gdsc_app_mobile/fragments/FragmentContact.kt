package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.dialogs.DialogFragmentContactMessage
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentContact : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val openDialog = view.findViewById<FloatingActionButton>(R.id.home_floating_button)
        openDialog.setOnClickListener {
            val dialog = DialogFragmentContactMessage()
            dialog.show(requireActivity().supportFragmentManager, "MessageDialog")
        }

        return view
    }
}