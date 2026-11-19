package com.example.khokhlov_zd2_v8

data class User(val name: String, val login: String)

data class DeliveryInfo(val fullName: String, val phone: String, val address: String)

data class Dish(val name: String, val price: Int)
