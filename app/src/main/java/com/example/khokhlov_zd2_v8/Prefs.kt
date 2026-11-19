package com.example.khokhlov_zd2_v8

import android.content.Context
import com.google.gson.Gson

object Prefs {
    private const val FILE = "food_prefs"
    private const val KEY_USER = "user_json"
    private const val KEY_DELIVERY = "delivery_json"
    private val gson = Gson()

    private fun sp(c: Context) = c.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun saveUser(c: Context, user: User) =
        sp(c).edit().putString(KEY_USER, gson.toJson(user)).apply()

    fun getUser(c: Context): User? =
        sp(c).getString(KEY_USER, null)?.let { gson.fromJson(it, User::class.java) }

    fun saveDelivery(c: Context, info: DeliveryInfo) =
        sp(c).edit().putString(KEY_DELIVERY, gson.toJson(info)).apply()

    fun getDelivery(c: Context): DeliveryInfo? =
        sp(c).getString(KEY_DELIVERY, null)?.let { gson.fromJson(it, DeliveryInfo::class.java) }

    fun logout(c: Context) = sp(c).edit().remove(KEY_USER).apply()
}
