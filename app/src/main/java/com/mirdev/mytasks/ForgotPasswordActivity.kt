package com.mirdev.mytasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.mirdev.mytasks.databinding.ActivityForgotPasswordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initListeners()
    }


    private fun initListeners() {
        binding.btnPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter an email address", Toast.LENGTH_LONG).show()
            } else {
                if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {

                    val database = FirebaseDatabase.getInstance()
                    val usersRef = database.getReference("users")
                    usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // El correo electrónico existe en la base de datos
                                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this@ForgotPasswordActivity,
                                                "Email sent successfully to reset your password",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@ForgotPasswordActivity,
                                                "Failed to send reset email. Please try again later.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            } else {
                                // El correo electrónico no existe en la base de datos
                                Toast.makeText(
                                    this@ForgotPasswordActivity,
                                    "Email not found",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Manejar errores de Firebase Realtime Database, si es necesario
                        }
                    })
                } else {
                    Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show()
                }
            }
        }
    }






}