package com.gregrdm.accukipweatherme.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gregrdm.accukipweatherme.R
import com.gregrdm.accukipweatherme.api.forecastmodel.FiveDayForecastModel
import com.gregrdm.accukipweatherme.databinding.WeatherDetailsFragmentBinding
import com.gregrdm.accukipweatherme.ui.details.adapter.FiveDaysWeatherRecyclerViewAdapter
import com.gregrdm.accukipweatherme.ui.utils.bindData

class WeatherDetailsFragment : Fragment() {

    lateinit var binding: WeatherDetailsFragmentBinding

    private lateinit var viewModel: WeatherDetailsViewModel

    private val args: WeatherDetailsFragmentArgs by navArgs()

    var weatherAdapter = FiveDaysWeatherRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(WeatherDetailsViewModel::class.java)
        binding = bindData<WeatherDetailsFragmentBinding>(inflater, container, R.layout.weather_details_fragment)
            .apply {
                lifecycleOwner = this@WeatherDetailsFragment.viewLifecycleOwner
                vm = viewModel
                adapter = weatherAdapter
            }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val locationKey = args.locationKey
        locationKey.let {
            viewModel.fetchFiveDayForecast(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenState.observe(this@WeatherDetailsFragment.viewLifecycleOwner, { screenState ->
            when (screenState) {
                is WeatherDetailsViewModel.ScreenState.ShowData -> fillData(screenState.data)
                is WeatherDetailsViewModel.ScreenState.ShowError -> showError(screenState.errorMessage)
            }
        })

        binding.forecastRecyclerView.adapter = weatherAdapter
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun fillData(forecast: FiveDayForecastModel) {
        binding.detailsHeadline.text = forecast.headline.text
        weatherAdapter.submitList(forecast.dailyForecasts)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(context, "Something went wrong $errorMessage", Toast.LENGTH_SHORT).show()
    }
}
