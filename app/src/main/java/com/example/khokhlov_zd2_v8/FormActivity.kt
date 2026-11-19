package com.example.khokhlov_zd2_v8

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etAddress = findViewById<EditText>(R.id.etAddress)

        Prefs.getDelivery(this)?.let {
            etFullName.setText(it.fullName)
            etPhone.setText(it.phone)
            etAddress.setText(it.address)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()

            etFullName.error = if (fullName.isEmpty()) getString(R.string.error_empty) else null
            etPhone.error = if (phone.isEmpty()) getString(R.string.error_empty) else null
            etAddress.error = if (address.isEmpty()) getString(R.string.error_empty) else null

            if (fullName.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()) {
                Prefs.saveDelivery(this, DeliveryInfo(fullName, phone, address))
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }
        }
    }
}
