package com.example.musicapplication_sp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.model.User
import com.example.musicapplication_sp.repositories.ApiService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.spotify.sdk.android.auth.AuthorizationClient

import com.spotify.sdk.android.auth.AuthorizationRequest

import com.spotify.sdk.android.auth.AuthorizationResponse



class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rQueue: RequestQueue
    private lateinit var editor: SharedPreferences.Editor
    private var numberOfAttempts: Int = 0

    companion object {
        private const val TAG = "ThirdPartyLogin"
        private const val RC_SIGN_IN = 9001
        private const val CLIENT_ID = "f9cb87049e144fc494ff35cc4091496c"
        private const val REQUEST_CODE = 1337
        private const val REDIRECT_URI = "https://com.example.musicapplication_sp//callback"
    }
    //field for firebase authentication
    private lateinit var auth: FirebaseAuth

    //fields declared for normal login
    private lateinit var loginButton: Button
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var timeCountField: TextView
    private lateinit var signSpotifyButton: Button
   //field declared for login in with google
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInGoogleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.login_layout)
        //initialize all the fields declared
        loginEmail = findViewById(R.id.text_email)
        loginPassword = findViewById(R.id.text_password)
        loginButton = findViewById(R.id.login_button)
        signInGoogleButton = findViewById(R.id.sign_in_google_button)
        signSpotifyButton = findViewById(R.id.sign_in_spotify_button)
        timeCountField = findViewById(R.id.time_count)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE)
        rQueue = Volley.newRequestQueue(this)

        this.logIn()//call function for signing with username/password
        this.listenToClickForSpotifySignIn() // call spotify function

        // Start Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(
            R.string.default_web_client_id
        )).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // END Configure Google Sign In

        this.listenToClickForGoogleSignIn()//call the google sign in function
    }

    /**
     * @description Overrides the OnStart and it gets called when the activity starts
     */
    override fun onStart() {
        super.onStart()
        // TODO" Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, update UI with the signed-in user's information
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    /**
     * @description
     * @param {String} The token to be used for authentication
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // TODO: Sign in success, update UI with the signed-in user's information
                    // TODO: open the user profile
                    // TODO: call a new intent
                    val user = auth.currentUser
                    Log.d(TAG, "signInWithGoogleCredential:success")
                    Toast.makeText(this@LoginActivity,"Signed in with google successful " + user!!.displayName + " " + user.email, Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithGoogleCredential:failure", task.exception)
                }
            }
    }

    /**
     * @description This function displays and connect to the google sign in page
     */
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * @description This function listens to the button for signing with google when clicked
     */
    private fun listenToClickForGoogleSignIn()
    {
        signInGoogleButton.setOnClickListener{
            signIn()
        }
    }

    private fun listenToClickForSpotifySignIn() {
        signSpotifyButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, SpotifyLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    /**
     * @description This function is used for logging in users with validate is the email and password is valid
     */
    private fun logIn() {
        loginButton.setOnClickListener {
            when {
                TextUtils.isEmpty(loginEmail.text.toString().trim { it <= ' ' }) -> { //the it <= '' remove all non printable characters with ascii code
                    Toast.makeText(this@LoginActivity, "Please enter email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(loginPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@LoginActivity, "Please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if (numberOfAttempts < 4) {
                        val email: String = loginEmail.text.toString().trim { it <= ' ' }
                        val password: String = loginPassword.text.toString().trim { it <= ' ' }

                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@LoginActivity, "You are logged in " + auth.currentUser!!.email, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("display_name", auth.currentUser!!.displayName)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Username or password not correct", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else if (numberOfAttempts == 4) {
                        Toast.makeText(this@LoginActivity, "Login failed. No of attempts is $numberOfAttempts.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login limit exceeded.", Toast.LENGTH_LONG).show()
                        object : CountDownTimer(60000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {

                                val  timeLeft = getString(R.string.time_count, millisUntilFinished / 1000)
                                timeCountField.text = timeLeft
                                loginButton.isEnabled = false //disable button

                            }

                            override fun onFinish() {
                                numberOfAttempts = 0
                                loginButton.isEnabled = true //enable button again
                                timeCountField.text = ""
                            }
                        }.start()

                    }
                    Log.d(TAG, numberOfAttempts.toString())
                    numberOfAttempts++
                }
            }

        }
    }
}