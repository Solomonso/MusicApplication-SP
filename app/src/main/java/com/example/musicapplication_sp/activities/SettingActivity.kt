package com.example.musicapplication_sp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapplication_sp.R
import com.google.zxing.*

class SettingActivity : AppCompatActivity() {
    private lateinit var signSpotifyButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var enabled2fa = false


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        signSpotifyButton = findViewById(R.id.sign_in_spotify_button)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE)
        this.listenToClickForSpotifySignInButton()
    }

    private fun listenToClickForSpotifySignInButton() {
        signSpotifyButton.setOnClickListener {
            val username = sharedPreferences.getString("username", "").toString()
            //check if the spotify user already linked an account with their username
            if (username.isEmpty()) {
                val intent = Intent(this@SettingActivity, SpotifyLoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@SettingActivity,
                    "Spotify Account already linked.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

//    private fun listenToClickForAddingTwoFactorAuth() {
//        enabled2fa = !enabled2fa
//
//        if(enabled2fa){
//            val bitmap =
//        }
//    }
}