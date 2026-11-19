package com.example.khokhlov_zd2_v8

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val user = Prefs.getUser(this)
        findViewById<View>(R.id.cardOrder).setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }
        findViewById<View>(R.id.cardDelivery).setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }
        findViewById<View>(R.id.cardProfile).setOnClickListener {
            val d = Prefs.getDelivery(this)
            val text = "Имя: ${user?.name}\nЛогин: ${user?.login}\n" +
                    "Телефон: ${d?.phone ?: "—"}\nАдрес: ${d?.address ?: "—"}"
            AlertDialog.Builder(this).setTitle(R.string.menu_profile)
                .setMessage(text).setPositiveButton("OK", null).show()
        }
        findViewById<View>(R.id.cardExit).setOnClickListener {
            Prefs.logout(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
