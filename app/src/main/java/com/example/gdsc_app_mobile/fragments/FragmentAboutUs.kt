package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity

class FragmentAboutUs : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_about_us, container, false)
        (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title).text = getString(
                    R.string.FragmentAboutUsTitle)

        return view
    }
}