package com.example.musicapplication_sp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapplication_sp.R

class SonglistActivity : AppCompatActivity() {

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        TODO("App crashes when 'songs' is selected in the nav-menu")
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onStop() {
        super.onStop()
    }

}