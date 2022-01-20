package com.example.musicapplication_sp.activities

import android.content.ContentValues.TAG
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rQueue: RequestQueue
    private var numberOfAttempts: Int = 0

    //field for firebase authentication
    private lateinit var auth: FirebaseAuth

    //fields declared for normal login
    private lateinit var loginButton: Button
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var timeCountField: TextView

    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.login_layout)
        //initialize all the fields declared
        loginEmail = findViewById(R.id.text_email)
        loginPassword = findViewById(R.id.text_password)
        loginButton = findViewById(R.id.login_button)

        registerButton = findViewById(R.id.register)
        timeCountField = findViewById(R.id.time_count)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE)
        rQueue = Volley.newRequestQueue(this)

        this.logIn()//call function for signing with username/password

        this.openRegistrationActivity() //open register page
    }

    /**
     * @description Overrides the OnStart and it gets called when the activity starts
     */
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }

    }

    private fun reload() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("display_name", auth.currentUser!!.email) //prevents the display from being null after reload
        startActivity(intent)
        finish()
    }


    private fun openRegistrationActivity()
    {
        registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
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
            val email: String = loginEmail.text.toString().trim { it <= ' ' } //the it <= '' remove all non printable characters with ascii code
            val password: String = loginPassword.text.toString().trim { it <= ' ' }
            when {
                TextUtils.isEmpty(email) -> {
                    Toast.makeText(this@LoginActivity, "Please enter email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(password) -> {
                    Toast.makeText(this@LoginActivity, "Please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if (numberOfAttempts < 4) {
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@LoginActivity, "You are logged in " + auth.currentUser!!.email, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("display_name", auth.currentUser!!.email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Username or password not correct", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else if (numberOfAttempts == 3) {
                        Toast.makeText(this@LoginActivity, "Login failed. No of attempts is $numberOfAttempts.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login limit exceeded.", Toast.LENGTH_LONG).show()
                        object : CountDownTimer(60000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {

                                val  timeLeft = getString(R.string.time_count, millisUntilFinished / 1000)
                                timeCountField.text = timeLeft
                                loginButton.isEnabled = false //disable button
                                registerButton.isEnabled = false

                            }

                            override fun onFinish() {
                                numberOfAttempts = 0
                                loginButton.isEnabled = true //enable button again
                                registerButton.isEnabled = true // enable register again
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