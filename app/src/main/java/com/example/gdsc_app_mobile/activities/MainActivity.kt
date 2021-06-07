package com.example.gdsc_app_mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.dialogs.DialogFragmentUserInfo
import com.example.gdsc_app_mobile.fragments.*
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    internal lateinit var toolbar: Toolbar
    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //instantiate drawer components
        setupDrawer()
        if (savedInstanceState == null) {
            addFragment(FragmentContact())
            navigationView.setCheckedItem(R.id.nav_contact)
        }
        val user_dialog = toolbar.findViewById<ImageView>(R.id.drawer_toolbar_user)
        user_dialog.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.on_click_animation))
            val dialog = DialogFragmentUserInfo()
            dialog.show(this.supportFragmentManager, "UserDialog")
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

        actionBarToggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout, toolbar,
                R.string.open,
                R.string.close
        )

        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_contact -> addFragment(FragmentContact())
            R.id.nav_about_us -> addFragment(FragmentAboutUs())
            R.id.nav_articles -> addFragment(FragmentArticles())
            R.id.nav_teams -> addFragment(FragmentTeams())
            R.id.nav_faq -> addFragment(FragmentFaq())
            R.id.nav_suggestions -> addFragment(FragmentSuggestions())
            R.id.nav_options -> addFragment(FragmentOptions())
            R.id.nav_logout -> {
                val intent = Intent(this@MainActivity, ActivitySignUp::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, fragment)
            .commit()
    }
}