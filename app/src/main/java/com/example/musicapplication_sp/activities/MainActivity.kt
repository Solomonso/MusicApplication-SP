package com.example.musicapplication_sp.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.Signature
import javax.crypto.Cipher

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var displayName: TextView
    private lateinit var username: String

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sign: Signature = Signature.getInstance("SHA256withRSA")

        //Creating KeyPair generator object
        val keyPairGen = KeyPairGenerator.getInstance("RSA")


        //Initializing the key pair generator
        keyPairGen.initialize(2048)

        //Generating the pair of keys
        val pair = keyPairGen.generateKeyPair()

        //Creating a Cipher object
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")

        //Initializing a Cipher object
        cipher.init(Cipher.ENCRYPT_MODE, pair.public)

        //Adding data to the cipher
        val input = "AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA".toByteArray()
        cipher.update(input)

        //encrypting the data
        val cipherText: ByteArray = cipher.doFinal()
        println(cipherText.toString() + "UTF8")

        toolbar = findViewById(R.id.toolbar)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE)
        displayName = findViewById(R.id.username)
        username = intent.getStringExtra("display_name").toString() //display the current user signed in
        displayName.text = username
        setSupportActionBar(toolbar)
        toggleDrawer()
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
                        val intent = Intent(this@MainActivity, PlaylistActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.settings -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.songs -> {
                        val intent = Intent(this@MainActivity, SonglistActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.logout -> {
                      Firebase.auth.signOut()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
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