package com.gregrdm.accukipweatherme.api

import com.gregrdm.accukipweatherme.api.model.City
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface AcuWeatherService {

    @GET("locations/v1/cities/search")
    fun searchCities(@Query(QUERY_API_KEY) key: String = API_KEY, @Query(QUERY_INPUT) query: String?, @Query(QUERY_OFFSET) offset: Int): Observable<List<City>>

    companion object {
        private const val API_KEY = "yLjl6EXPAhO8uRV4tQdTDl6i3cyuYr5o" //"33dTNkeAwbvCFTaXzRmz8VP3F5rYBKkp"
        private const val QUERY_API_KEY = "apikey"
        private const val QUERY_INPUT = "q"
        private const val QUERY_OFFSET = "offset"
    }
}
