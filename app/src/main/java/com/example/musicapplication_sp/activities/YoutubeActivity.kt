package com.example.musicapplication_sp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.activities.YoutubeActivity
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistSnippet
import com.google.api.services.youtube.model.PlaylistStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException

class YoutubeActivity : AppCompatActivity() {
    private val mCredential: GoogleAccountCredential? = null
    private val credential: Credential? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                initializeYoutubeApi()
            } catch (e: Exception){
                e.message
            }
        }
        Log.d("Test", "Test")
    }

    @Throws(IOException::class)
    private fun authorize(httpTransport: NetHttpTransport): Credential {
        // Load client secrets.
        val assets = this.assets
        val `in` = YoutubeActivity::class.java.getResourceAsStream(CLIENT_SECRETS)
        val clientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY,
            InputStreamReader(assets.open("client_secret.json"))
        )
        // Build flow and trigger user authorization request.
        val flow =
            GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                SCOPES
            )
                .build()
        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }

    @get:Throws(GeneralSecurityException::class, IOException::class)
    val service: YouTube
        get() {
            val httpTransport = NetHttpTransport()
            var credential = authorize(httpTransport)

            return YouTube.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
    APPLICATION_NAME
    ).build()
        }

    fun initializeYoutubeApi() {
        try {
            val youtubeService = service
            val playlist = Playlist()

            // Add the snippet object property to the Playlist object.
            val snippet = PlaylistSnippet()
            snippet.defaultLanguage = "en"
            snippet.description = "This is a sample playlist description."
            snippet.title = "Music app playlist 2"
            playlist.snippet = snippet

            // Add the status object property to the Playlist object.
            val status = PlaylistStatus()
            status.privacyStatus = "private"
            playlist.status = status

            // Define and execute the API request
            CoroutineScope(Dispatchers.IO).launch {
                val request = youtubeService.playlists().insert("snippet,status", playlist)
                val response = request.execute()
                Log.d("response ", response.toString())
            }
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        //
        //    private static final String PREF_ACCOUNT_NAME = "accountName";
        //    private static final int REQUEST_ACCOUNT_PICKER = 1000;
        //    private static final int REQUEST_AUTHORIZATION = 1001;
        //    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
        //    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
        //    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_UPLOAD};
        private const val CLIENT_SECRETS = "client_secret.json"
        private val SCOPES: Collection<String> =
            listOf("https://www.googleapis.com/auth/youtube.force-ssl")

        //AssetManager assets = this.getAssets();
        private const val APPLICATION_NAME = "API code samples"
        private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
    }
}