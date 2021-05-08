package com.gregrdm.accukipweatherme.api.model

import com.google.gson.annotations.SerializedName

data class GeoPosition(
    @SerializedName("Elevation")
    val elevation: Elevation,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double
) {
    data class Elevation(
        @SerializedName("Imperial")
        val imperial: Imperial,
        @SerializedName("Metric")
        val metric: Metric
    ) {
        data class Imperial(
            @SerializedName("Unit")
            val unit: String,
            @SerializedName("UnitType")
            val unitType: Int,
            @SerializedName("Value")
            val value: Int
        )

        data class Metric(
            @SerializedName("Unit")
            val unit: String,
            @SerializedName("UnitType")
            val unitType: Int,
            @SerializedName("Value")
            val value: Int
        )
    }
}
