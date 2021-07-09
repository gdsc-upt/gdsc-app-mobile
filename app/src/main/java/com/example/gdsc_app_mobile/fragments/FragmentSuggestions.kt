package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.RVAdapterFaq
import com.example.gdsc_app_mobile.interfaces.OnItemClickListener
import com.example.gdsc_app_mobile.models.FaqModel

class FragmentSuggestions : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_suggestions, container, false)
        val toolbar = (activity as MainActivity).toolbar
        HelperClass.setToolbarStyle(context, toolbar, "suggestions")


        return view
    }

}