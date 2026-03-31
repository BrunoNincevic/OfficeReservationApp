package com.example.officereservationapp.model

data class User(
    val userId: String = "",
    val userName: String = "",
    val department: String = "",
    val userEmail: String = "",
    val reservedDeskId: Int? = null

)