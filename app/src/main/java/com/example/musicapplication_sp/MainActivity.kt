package com.example.musicapplication_sp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggleDrawer()

//        popUp()
    }

    private fun toggleDrawer()
    {
        val menubar = findViewById<ImageView>(R.id.menubar)
        menubar.setOnClickListener {

            val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
            val navView : NavigationView = findViewById(R.id.navigation_view)

            toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.profile -> {
                        Toast.makeText(
                            applicationContext,
                            "Opened profile information",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.playlist -> {
                        Toast.makeText(applicationContext, "List of playlists", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }
                    R.id.settings -> {
                        Toast.makeText(applicationContext, "Opened Settings", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }
                    R.id.logout -> {
                        Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }
            }
        }
    }

//    private fun popUp()
//    {
//        val clickList = findViewById<ImageView>(R.id.add_playlist)
//        clickList.setOnClickListener {
//
//            val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
//            val addPlaylist : NavigationView = findViewById(R.id.navigation_addPlaylist)
//
//            toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
//            drawerLayout.addDrawerListener(toggle)
//            toggle.syncState()
//
//            addPlaylist.setNavigationItemSelectedListener {
//
//            }
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}