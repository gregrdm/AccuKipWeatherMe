package com.gregrdm.accukipweatherme.api.forecastmodel


import com.google.gson.annotations.SerializedName

data class Night(
    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean,
    @SerializedName("Icon")
    val icon: Int,
    @SerializedName("IconPhrase")
    val iconPhrase: String,
    @SerializedName("PrecipitationIntensity")
    val precipitationIntensity: String,
    @SerializedName("PrecipitationType")
    val precipitationType: String
)
