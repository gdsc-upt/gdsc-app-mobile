package com.example.gdsc_app_mobile

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var drawerLayout : DrawerLayout

    lateinit var navigationView : NavigationView
    lateinit var toolbar : Toolbar
    lateinit var actionBarToggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //instantiate drawer components
        setupDrawer()

        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container_fragment,
                    FragmentContact()
                )
                .commit()
            navigationView.setCheckedItem(R.id.nav_contact)
        }

        setupMode()
    }

    private fun setupMode() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("save", 0)
        val state: Boolean = sharedPreferences.getBoolean("dark_mode", true)
        val language = sharedPreferences.getString("language","en")

        val lastFragment = sharedPreferences.getString("lastFragment","aboutUs")

        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if (language != resources.configuration.locale.toString()) {
            val locale = Locale(language)
            val res: Resources = resources
            val dm: DisplayMetrics = res.getDisplayMetrics()
            val conf: Configuration = res.getConfiguration()
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(this,MainActivity::class.java)
            refresh.putExtra("lang",locale)
            startActivity(refresh)
            val editor = sharedPreferences.edit()
            editor.putString("language",language)
            editor.apply()
        }
        lastFragment?.let { Log.d("lastFrag", it) }
        when (lastFragment){
            "aboutUs" -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.container_fragment,
                            AboutUsFragment()
                        )
                        .commit()
            }

            "options" -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container_fragment,
                        FragmentOptions()
                    )
                    .commit()
            }
        }


    }

    private fun setupDrawer() {
        // set toolbar
        toolbar = findViewById(R.id.nav_toolbar_toolbar)
        setSupportActionBar(toolbar)
        val title = findViewById<TextView>(R.id.toolbar_title)
        title.text = getString(R.string.app_name)
        // get layout
        drawerLayout = findViewById(R.id.drawer_layout)

        // create navigationView's components
        navigationView = findViewById(R.id.navigation)
        navigationView.setNavigationItemSelectedListener(this)

        // add cropped image
        val header = navigationView.getHeaderView(0)
        val navHeaderImgView = header.findViewById<ImageView>(R.id.nav_header_img_view)
        Picasso.get()
            .load(R.drawable.img_dsc)
            .transform(CropCircleTransformation())
            .into(navHeaderImgView)
        val navHeaderTitle = header.findViewById<TextView>(R.id.nav_header_title)
        navHeaderTitle.textSize = 20f


        actionBarToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_contact -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container_fragment,
                        FragmentContact()
                    )
                    .commit()
            }
            R.id.nav_about_us -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container_fragment,
                        AboutUsFragment()
                    )
                    .commit()
            }
            R.id.nav_articles -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(
                                R.id.container_fragment,
                                FragmentArticles()
                        )
                        .commit()
            }

            R.id.nav_teams -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(
                                R.id.container_fragment,
                                FragmentTeams()
                        )
                        .commit()
            }
            R.id.nav_faq -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(
                                    R.id.container_fragment,
                                    FragmentFaq()
                            )
                            .commit()
                }

            R.id.nav_suggestions -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(
                                R.id.container_fragment,
                                FragmentSuggestions()
                        )
                        .commit()
            }
            R.id.nav_options -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container_fragment,
                        FragmentOptions()
                    )
                    .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}