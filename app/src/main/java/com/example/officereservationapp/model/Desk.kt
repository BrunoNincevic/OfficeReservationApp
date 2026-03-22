package com.example.officereservationapp.model

data class Desk (
    var id: Int = 0,
    var xCoordinatePercentage: Float = 0f,
    var yCoordinatePercentage: Float = 0f,
    var reservedByUser: String? = null

)