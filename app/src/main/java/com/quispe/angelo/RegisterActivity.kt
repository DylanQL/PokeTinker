package com.quispe.angelo

import SharedPreferencesRepository
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quispe.angelo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesRepository = SharedPreferencesRepository().apply {
            setSharedPreference(this@RegisterActivity)
        }

        setupButtons()
    }

    private fun setupButtons() {
        // Back button
        binding.btnBackClose.setOnClickListener {
            finish()  // Closes the current activity and returns to the previous one
        }

        // Register button
        binding.btnRegister.setOnClickListener {
            registerUser(it)  // Calls the register function when the button is clicked
        }

        // "Already have an account" button
        binding.btnAlreadyAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))  // Redirects to the login
        }
    }

    private fun registerUser(view: View) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtPassword2.text.toString()

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (!arePasswordsMatching(password, confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPreferencesRepository.saveUserEmail(email)
        sharedPreferencesRepository.saveUserPassword(password)

        Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()  // Ends the current activity so it doesn’t go back to registration on pressing back
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}
