package com.example.musicapplication_sp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var numberOfAttempts: Int = 0

    companion object {
        private const val TAG = "ThirdPartyLogin"
        private const val RC_SIGN_IN = 9001
    }
    //field for firebase authentication
    private lateinit var auth: FirebaseAuth

    //fields declared for normal login
    private lateinit var loginButton: Button
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var timeCountField: TextView
   //field declared for login in with google
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInGoogleButton: Button
    private lateinit var callbackManager: CallbackManager
    private lateinit var signInFacebookButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.login_layout)
        //initialize all the fields declared
        loginEmail = findViewById(R.id.text_email)
        loginPassword = findViewById(R.id.text_password)
        loginButton = findViewById(R.id.login_button)
        signInGoogleButton = findViewById(R.id.sign_in_google_button)
        signInFacebookButton = findViewById(R.id.sign_in_facebook_button)
        timeCountField = findViewById(R.id.time_count)

        this.logIn()//call function for signing with username/password

        // Start Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // END Configure Google Sign In

        this.listenToClickForGoogleSignIn()//call the google sign in function

        //Start initialize the facebook login button
        callbackManager = CallbackManager.Factory.create()
        signInFacebookButton.setOnClickListener{
            LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, listOf("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)
                }
            })
            //END initialize the facebook login button
        }
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
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
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

    /**
     * @description
     * @param {AccessToken} The token to be used for authentication
     */
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // TODO: Sign in success, update UI with the signed-in user's information
                    // TODO: open the user profile
                    // TODO: call a new intent
                    val user = auth.currentUser
                    Log.d(TAG, "signInWithFacebookCredential:success")
                    Toast.makeText(this@LoginActivity,"Signed in with facebook successful " + user!!.displayName + " " + user.email, Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithFacebookCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",Toast.LENGTH_SHORT).show()
                }
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
                                Log.e("Logged in", auth.currentUser!!.uid)
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