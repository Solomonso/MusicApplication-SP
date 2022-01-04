package com.example.musicapplication_sp

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var btnPlaySong : Button
    private lateinit var btnPauseSong : Button
    var mediaPlayer : MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPauseSong = findViewById(R.id.pauseSong)
        btnPlaySong = findViewById(R.id.playSong)

        btnPlaySong.setOnClickListener {
            playaudio()
        }

        btnPauseSong.setOnClickListener {
            pauseaudio()
        }
    }


    private fun playaudio() {
        val audioURL ="https://www.youtube.com/watch?v=W4hTJybfU7s"

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try{
             mediaPlayer!!.setDataSource(audioURL)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch(e : IOException){
            e.printStackTrace()
        }
        Toast.makeText(this, "Songs started plating", Toast.LENGTH_LONG).show()

    }
    private fun pauseaudio(){
        if(mediaPlayer!!.isPlaying){
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
        } else{
            Toast.makeText(this,"There is no song playing", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
    }

}



