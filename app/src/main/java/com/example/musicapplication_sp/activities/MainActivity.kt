package com.example.musicapplication_sp.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.musicapplication_sp.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var displayName: TextView
    private lateinit var username: String

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_main)

        toolbar = findViewById(R.id.toolbar)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE)
        displayName = findViewById(R.id.username)
        username = intent.getStringExtra("display_name").toString()
        username  = sharedPreferences.getString("username", "No user").toString() //display the current user signed in
        displayName.text = username
        setSupportActionBar(toolbar)
        toggleDrawer()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun connected() {

    }

    override fun onStop() {
        super.onStop()
    }

    private fun displayUserDetail(name: String) {
        var returnValue = ""
        when (name) {
            intent.getStringExtra(name) -> {returnValue = name }
            sharedPreferences.getString(name, "No user") -> {returnValue = name }
            else -> {}
        }

    }
    private fun toggleDrawer()
    {
          val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
          val navView : NavigationView = findViewById(R.id.navigation_view)

          toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
          toggle.isDrawerIndicatorEnabled = true
          drawerLayout.addDrawerListener(toggle)
          toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    Toast.makeText(applicationContext, "Opened profile information", Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.playlist -> {
                        val intent = Intent(this@MainActivity, ActivityPlaylist::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        true
                    }
                    R.id.settings -> {
                        Toast.makeText(applicationContext, "Opened Settings", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }
                    R.id.logout -> {
                      Firebase.auth.signOut()
                        true
                    }
                    else -> true
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}