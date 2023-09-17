package com.mirdev.mytasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.google.firebase.auth.FirebaseAuth
import com.mirdev.mytasks.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //checkUser()
        initUI()
    }

    private fun initUI() {

        logIn()
        signUp()
        resetPassword()
    }




    private fun logIn() {
        val email = binding.etEmail.text
        val password = binding.etPassword.text
        binding.btnLogIn.setOnClickListener {
            if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
                // Validar el correo electrónico aquí
                if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(), password.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                // Autenticación exitosa, mostrar la página principal
                                showHome()
                            } else {
                                // Autenticación fallida, mostrar mensaje de error
                                Toast.makeText(
                                    this,
                                    "Email or password incorrect",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    // Correo electrónico no válido, mostrar mensaje de error
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
                }
            } else {
                // Campos vacíos, mostrar mensaje de error
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signUp() {
        binding.tvSignup.setOnClickListener {
            val intent = Intent (this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
   /* private fun checkUser() {
        // Verifica si el usuario ya ha iniciado sesión
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            showHome()
            finish()

        }
    }*/

    private fun resetPassword() {
       val resetPassword = binding.tvForgotPassword
        resetPassword.setOnClickListener {
            val intent = Intent (this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    private fun showHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


}