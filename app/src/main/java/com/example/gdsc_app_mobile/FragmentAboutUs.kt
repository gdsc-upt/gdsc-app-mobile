package com.example.gdsc_app_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class FragmentAboutUs : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)
        saveLastFragment()
        return view
    }

    private fun saveLastFragment(){
        val editor = activity!!.getSharedPreferences("save", 0).edit()
        editor.putString("lastFragment","aboutUs")
        editor.apply()
    }

}