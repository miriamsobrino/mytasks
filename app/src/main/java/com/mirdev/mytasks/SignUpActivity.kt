package com.mirdev.mytasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.google.firebase.auth.FirebaseAuth
import com.mirdev.mytasks.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        signIn()
    }

    private fun signIn() {
        val signInBtn = binding.btnSignIn
        val email = binding.etEmail.text
        val password = binding.etPassword.text
        signInBtn.setOnClickListener {
            if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
                if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.toString(), password.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome()
                            } else {

                            }
                        }
                }else{
                    Toast.makeText(baseContext, "Please enter a valid email address", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(baseContext, "Empty fields are not allowed", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    private fun showHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}