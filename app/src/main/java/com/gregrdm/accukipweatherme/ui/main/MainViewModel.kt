package com.gregrdm.accukipweatherme.ui.main

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gregrdm.accukipweatherme.api.AcuWeatherService
import com.gregrdm.accukipweatherme.api.ServiceBuilder
import com.gregrdm.accukipweatherme.api.model.City
import com.gregrdm.accukipweatherme.ui.utils.matchesWithMyRegex
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

    lateinit var inputText: String

    init {
        setupAutoComplete()
    }

    fun fetchCities(input: String) {
        if (matchesWithMyRegex(input)) {
            autoCompletePublishSubject.accept(input.trim())
            inputText = input.trim()
        } else if (input.isNotEmpty()) {
            mutableScreenState.postValue(ScreenState.ShowError("Unsupported City name, use ENGLISH names."))
        }
    }

    private fun setupAutoComplete() = autoCompletePublishSubject
        .debounce(300, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .filter { it.length >= 3 }
        .switchMap { getCities(it) }
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
        .doOnError {
            mutableScreenState.postValue(ScreenState.ShowError(it?.localizedMessage!!))
        }
        .toObservable()

    sealed class ScreenState {
        class ShowData(val data: List<City>) : ScreenState()
        class ShowError(val errorMessage: String) : ScreenState()
    }
}
