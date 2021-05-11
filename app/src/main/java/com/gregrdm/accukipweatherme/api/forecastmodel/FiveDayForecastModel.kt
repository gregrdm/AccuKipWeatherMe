package com.gregrdm.accukipweatherme.api.forecastmodel


import com.google.gson.annotations.SerializedName

data class FiveDayForecastModel(
    @SerializedName("DailyForecasts")
    val dailyForecasts: List<DailyForecast>,
    @SerializedName("Headline")
    val headline: Headline
)
