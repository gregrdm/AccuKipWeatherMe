package com.gregrdm.accukipweatherme.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gregrdm.accukipweatherme.api.AcuWeatherService
import com.gregrdm.accukipweatherme.api.ServiceBuilder
import com.gregrdm.accukipweatherme.api.model.City
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    val screenState: LiveData<ScreenState> by lazy { mutableScreenState }

    private val mutableScreenState = MutableLiveData<ScreenState>()

    private val autoCompletePublishSubject = PublishRelay.create<String>()

    init {
        setupAutoComplete()
    }

    fun fetchCities(input: String?) {
        if (input != null) autoCompletePublishSubject.accept(input.trim())
    }

    private fun setupAutoComplete() = autoCompletePublishSubject
        .debounce(300, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .filter { it.length > 5 } // todo: remove or decrease length after development
        .switchMap { getCities(it.toString()) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { result -> mutableScreenState.postValue(ScreenState.ShowData(result)) },
            { t: Throwable? -> mutableScreenState.postValue(ScreenState.ShowError(t?.localizedMessage!!)) }
        )

    private fun getCities(query: String): @NonNull Observable<MutableList<City>>? = ServiceBuilder.buildService(AcuWeatherService::class.java)
        .searchCities(query = query, offset = 15)
        .flatMapIterable { it }
        .toList()
        .onErrorReturn { emptyList() }
        .toObservable()

    fun onCityItemClicked() {

    }

    sealed class ScreenState {
        class ShowData(val data: List<City>) : ScreenState()
        class ShowError(val errorMessage: String) : ScreenState()
        object NoData : ScreenState()
    }
}
