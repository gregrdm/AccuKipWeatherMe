package com.gregrdm.accukipweatherme.api.forecastmodel


import com.google.gson.annotations.SerializedName

data class Minimum(
    @SerializedName("Unit")
    val unit: String,
    @SerializedName("UnitType")
    val unitType: Int,
    @SerializedName("Value")
    val value: Float
)
