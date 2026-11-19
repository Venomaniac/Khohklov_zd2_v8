package com.example.khokhlov_zd2_v8

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Prefs.getUser(this) != null) {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            etEmail.error = when {
                email.isEmpty() -> getString(R.string.error_empty)
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> getString(R.string.error_email)
                else -> null
            }
            etPassword.error = if (password.isEmpty()) getString(R.string.error_empty) else null

            if (etEmail.error == null && etPassword.error == null) {
                Prefs.saveUser(this, User(name = email.substringBefore('@'), login = email))
                startActivity(Intent(this, FormActivity::class.java))
                finish()
            }
        }
    }
}
