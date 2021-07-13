package com.example.gdsc_app_mobile.fragments

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.activities.MainActivity
import java.util.*

class FragmentOptions : Fragment() {
    private lateinit var switchDarkMode: SwitchCompat
    private lateinit var changeLanguage: LinearLayout
    private lateinit var changeLanguagePicture: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)
        val toolbar = (activity as MainActivity).toolbar
        HelperClass.setToolbarStyle(context, toolbar, "options")

        switchDarkMode = view.findViewById(R.id.switch_dark_mode)

        setupMode()

        changeLanguagePicture = view.findViewById(R.id.options_change_language_picture)
        changeLanguage = view.findViewById(R.id.options_change_language)
        changeLanguage.setOnClickListener {
            showChangeLang()
        }

        setupLanguage()

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

    private fun showChangeLang() {

        val listItems = arrayOf("English", "Romana")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialog, which ->
            if (which == 0) {
                Singleton.setLanguage("en")
                setLocate("en")
                recreate(requireActivity())
            } else if (which == 1) {
                Singleton.setLanguage("ro")

                setLocate("ro")
                recreate(requireActivity())
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()

    }
    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

    private fun setupLanguage(){
        val lng = Singleton.getLanguage()
        if(lng == "en"){
            changeLanguagePicture.setImageResource(R.drawable.flags_america)
        }else if(lng == "ro"){
            changeLanguagePicture.setImageResource(R.drawable.flags_romania)
        }
    }
}