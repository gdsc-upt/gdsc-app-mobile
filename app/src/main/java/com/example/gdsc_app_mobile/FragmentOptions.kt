package com.example.gdsc_app_mobile

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import java.util.*


class FragmentOptions : Fragment(), AdapterView.OnItemSelectedListener{

    lateinit var switch_dark_mode: SwitchCompat
    lateinit var language_spinner : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)

        switch_dark_mode = view.findViewById(R.id.switch_dark_mode)
        language_spinner = view.findViewById(R.id.language_spinner)
        language_spinner.onItemSelectedListener = this;


        activity!!.getSharedPreferences("save", 0).edit().putString("lastFragment","options")
        activity!!.getSharedPreferences("save", 0).edit().commit()



        setupMode()
        setupLanguage()
        return view
    }

    private fun changeLanguage(language : String?){
        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences("save", 0)

        val pref_language = sharedPreferences.getString("language","en");



        if (pref_language != language) {
            var myLocale = Locale("en")
            when (language) {
                "en" -> {
                    myLocale = Locale("en")
                }
                "ro" -> {
                    myLocale = Locale("ro")
                }

            }

            val res: Resources = resources
            val dm: DisplayMetrics = res.getDisplayMetrics()
            val conf: Configuration = res.getConfiguration()
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(this.context,MainActivity::class.java)
            startActivity(refresh)
            val editor = sharedPreferences.edit()
            editor.putString("language",language)
            editor.apply()
        }

    }

    private fun setupLanguage() {
        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences("save", 0)

        this.context?.let {
            ArrayAdapter.createFromResource(it,R.array.Languages,R.layout.language_spinner_item).also { adapter ->
                adapter.setDropDownViewResource(R.layout.language_spinner_item)
                language_spinner.adapter = adapter
            }
        }

        val language = sharedPreferences.getString("language","en");
        changeLanguage(language)

        when (language)
        {
            "en" -> {language_spinner.setSelection(0)}
            "ro" -> {language_spinner.setSelection(1)}
        }

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


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position)
        {
            0 -> {changeLanguage("en")}
            1-> {changeLanguage("ro")}
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}