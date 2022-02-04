package com.example.musicapplication_sp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapplication_sp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repeatPassword: EditText
    lateinit var fName: EditText
    lateinit var lName: EditText
    lateinit var passwordCriteria: TextView
    lateinit var signUpButton: Button
    var db = Firebase.firestore

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        email = findViewById(R.id.editSignUpTextEmailAddress)
        password = findViewById(R.id.editSignUpTextPassword)
        repeatPassword = findViewById(R.id.editTextRepeatPassword)
        fName = findViewById(R.id.editTextFName)
        lName = findViewById(R.id.editTextLName)
        passwordCriteria = findViewById(R.id.registration_error_message)
        signUpButton = findViewById(R.id.signUp)
        signUpButton.setOnClickListener {
            registerUser()
        }
    }


    private fun registerUser() {
        val email: String = email.text.toString().trim { it <= ' ' }
        val password: String = password.text.toString().trim { it <= ' ' }
        val repeatPassword: String = repeatPassword.text.toString().trim { it <= ' ' }
        val fName: String = fName.text.toString()
        val lName: String = lName.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (!isValidEmail(email)) {
                passwordCriteria.text =
                    getString(R.string.enter_valid_email)
            } else if (!isValidPassword(password)) {
                passwordCriteria.text =
                    getString(R.string.invalid_password_message)
            } else if (password != repeatPassword) {
                passwordCriteria.text =
                    getString(R.string.password_criteria)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Your account is created with the email address " + auth.currentUser!!.email,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Create a new user with a first and last name
                                    val user = Firebase.auth.currentUser
                                    user?.let {
                                        val userData = hashMapOf(
                                            "UserID" to user.uid,
                                            "email" to email,
                                            "firstName" to fName,
                                            "lastName" to lName,
                                        )
                                        // Add a new document with a generated ID
                                        db.collection("Users")
                                            .add(userData)
                                            .addOnSuccessListener { documentReference ->
                                                Log.d(
                                                    TAG,
                                                    "DocumentSnapshot added with ID: ${documentReference.id}"
                                                )
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(TAG, "Error adding document", e)
                                            }
                                    }
                                    auth.signOut()
                                    val intent =
                                        Intent(this@RegistrationActivity, LoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                } else {
                                    passwordCriteria.text = ""
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Account couldn't be created: " + (task.exception?.message
                                            ?: String),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RegistrationActivity,
                                e.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || fName.isEmpty() || lName.isEmpty()) {
            Toast.makeText(
                this@RegistrationActivity,
                "One or more fields are not filled in",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern

        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"

        pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password)

        return matcher.matches()
    }


}



