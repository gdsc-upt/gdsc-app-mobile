package com.example.gdsc_app_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

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

    }

    private fun setupDrawer() {
        // set toolbar
        toolbar = findViewById(R.id.nav_toolbar_toolbar)
        setSupportActionBar(toolbar)
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
            R.id.nav_articles ->{
                supportFragmentManager
                        .beginTransaction()
                        .replace(
                                R.id.container_fragment,
                                FragmentArticles()
                        )
                        .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}