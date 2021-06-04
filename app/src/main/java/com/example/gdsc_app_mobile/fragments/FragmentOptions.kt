package com.example.gdsc_app_mobile.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity

class FragmentOptions : Fragment() {
    private lateinit var switchDarkMode: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)
        (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title).text = getString(
                    R.string.FragmentOptionsTitle)

        switchDarkMode = view.findViewById(R.id.switch_dark_mode)

        setupMode()

        return view
    }

    private fun setupMode() {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("save", 0)

        switchDarkMode.isChecked = sharedPreferences.getBoolean("dark_mode", false)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                val editor: SharedPreferences.Editor =
                    requireActivity().getSharedPreferences("save", 0).edit()
                editor.putBoolean("dark_mode", true)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                val editor: SharedPreferences.Editor =
                    requireActivity().getSharedPreferences("save", 0).edit()
                editor.putBoolean("dark_mode", false)
                editor.apply()
            }
        }
    }
}