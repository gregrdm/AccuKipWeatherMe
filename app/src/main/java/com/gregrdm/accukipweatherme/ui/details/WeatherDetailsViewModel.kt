package com.gregrdm.accukipweatherme.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gregrdm.accukipweatherme.api.AcuWeatherService
import com.gregrdm.accukipweatherme.api.ServiceBuilder
import com.gregrdm.accukipweatherme.api.forecastmodel.FiveDayForecastModel
import io.reactivex.rxjava3.disposables.Disposable

class WeatherDetailsViewModel : ViewModel() {

    val screenState: LiveData<ScreenState> by lazy { mutableScreenState }

    private val mutableScreenState = MutableLiveData<ScreenState>()

    fun fetchFiveDayForecast(locationKey: String): Disposable =
        ServiceBuilder.buildService(AcuWeatherService::class.java)
            .get5DayForecast(locationKey = locationKey)
            .subscribe(
                { result -> mutableScreenState.postValue(ScreenState.ShowData(result)) },
                { t: Throwable? -> mutableScreenState.postValue(ScreenState.ShowError(t?.localizedMessage!!)) }
            )

    sealed class ScreenState {
        class ShowData(val data: FiveDayForecastModel) : ScreenState()
        class ShowError(val errorMessage: String) : ScreenState()
    }
}
