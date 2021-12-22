package com.example.musicapplication_sp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var signUpButton : Button
    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_layout)
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        email = findViewById(R.id.editSignUpTextEmailAddress)
        password = findViewById(R.id.editSignUpTextPassword)
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
        val email = email.text.toString()
        val password = password.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password)
                    // Create a new user with a first and last name
                    val user = hashMapOf(
                        "UserID" to auth.uid,
                        "firstName" to "Ramon",
                        "lastName" to "Brakels",
                    )

                    // Add a new document with a generated ID
                    db.collection("Users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                    withContext(Dispatchers.Main) {
                        //checkLoggedInState()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
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

}



