package com.example.musicapplication_sp.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapplication_sp.R
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var fName: EditText
    lateinit var lName: EditText
    lateinit var signUpButton: Button

    var db = Firebase.firestore

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        auth = FirebaseAuth.getInstance()
        //auth.signOut()
        email = findViewById(R.id.editSignUpTextEmailAddress)
        password = findViewById(R.id.editSignUpTextPassword)
        fName = findViewById(R.id.editTextFName)
        lName = findViewById(R.id.editTextLName)
        signUpButton = findViewById(R.id.signUp)
        signUpButton.setOnClickListener {
            registerUser()
        }
//        this.createSignIntent()
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun registerUser() {
        val email: String = email.text.toString()
        val password: String = password.text.toString()
        val fName: String = fName.text.toString()
        val lName: String = lName.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    auth.createUserWithEmailAndPassword(email, password)
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
                        withContext(Dispatchers.Main) {
                            //checkLoggedInState()
                        }
                    }


                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegistrationActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    /* private fun checkLoggedInState() {
         if (auth.currentUser == null) { // not logged in
             tvLoggedIn.text = "You are not logged in"
         } else {
             tvLoggedIn.text = "You are logged in!"
         }
     }
 */
    override fun onStart() {
        super.onStart()
        //checkLoggedInState()
    }

    private fun encrypt() {
//        String encrypted = AES
    }

    private fun decrypt() {

    }
}



