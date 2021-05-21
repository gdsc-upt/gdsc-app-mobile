package com.example.gdsc_app_mobile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment


class FragmentOptions : Fragment() {

    lateinit var switch_dark_mode: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)

        switch_dark_mode = view.findViewById(R.id.switch_dark_mode)

        setupMode()

        return view
    }

    private fun setupMode() {
        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences("save", 0)

        switch_dark_mode.isChecked = sharedPreferences.getBoolean("dark_mode", true)

        if (switch_dark_mode.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        switch_dark_mode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                val editor: SharedPreferences.Editor =
                    activity!!.getSharedPreferences("save", 0).edit()
                editor.putBoolean("dark_mode", true)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                val editor: SharedPreferences.Editor =
                    activity!!.getSharedPreferences("save", 0).edit()
                editor.putBoolean("dark_mode", false)
                editor.apply()
            }
        }
    }
}