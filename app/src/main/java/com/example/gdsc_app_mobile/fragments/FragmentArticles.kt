package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity

class FragmentArticles : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        val toolbar = (activity as MainActivity).toolbar
        HelperClass.setToolbarStyle(context, toolbar, "articles")
        return view
    }
}