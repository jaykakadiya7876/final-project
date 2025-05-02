package com.demo.foodorderanddeliveryappkotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.demo.foodorderanddeliveryappkotlin.activities.BaseActivity
import com.demo.foodorderanddeliveryappkotlin.activities.RestaurantListActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : BaseActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toggleView: TextView
    private lateinit var mAuth: FirebaseAuth
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        if (mAuth.currentUser != null) {
            // User is already logged in, navigate to MainActivity
            startMainActivity()
            return
        }

        // Initialize UI components
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        registerButton = findViewById(R.id.buttonRegister)
        progressBar = findViewById(R.id.progressBar)
        toggleView = findViewById(R.id.textViewToggle)

        updateUI()

        // Set click listeners
        toggleView.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUI()
        }

        loginButton.setOnClickListener {
            loginUser()
        }

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun updateUI() {
        if (isLoginMode) {
            loginButton.visibility = View.VISIBLE
            registerButton.visibility = View.GONE
            toggleView.text = getString(R.string.no_account_sign_up)
        } else {
            loginButton.visibility = View.GONE
            registerButton.visibility = View.VISIBLE
            toggleView.text = getString(R.string.already_have_account_login)
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = emailEditText.text.toString()
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Required."
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Enter a valid email."
            valid = false
        } else {
            emailEditText.error = null
        }

        val password = passwordEditText.text.toString()
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Required."
            valid = false
        } else if (password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters."
            valid = false
        } else {
            passwordEditText.error = null
        }

        return valid
    }

    private fun loginUser() {
        if (!validateForm()) {
            return
        }

        showProgressBar()

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    // Sign in success
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser() {
        if (!validateForm()) {
            return
        }

        showProgressBar()

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    // Sign up success
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        loginButton.isEnabled = false
        registerButton.isEnabled = false
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        loginButton.isEnabled = true
        registerButton.isEnabled = true
    }

    private fun startMainActivity() {
        // Direct navigation to RestaurantListActivity using the navigateTo method
        navigateTo(RestaurantListActivity::class.java)
        finish()
    }
}
