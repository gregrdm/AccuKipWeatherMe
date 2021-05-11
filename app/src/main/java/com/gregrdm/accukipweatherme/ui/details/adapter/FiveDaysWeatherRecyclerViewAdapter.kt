package com.gregrdm.accukipweatherme.ui.details.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gregrdm.accukipweatherme.R
import com.gregrdm.accukipweatherme.api.forecastmodel.DailyForecast
import com.gregrdm.accukipweatherme.databinding.ListWeatherForecastItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FiveDaysWeatherRecyclerViewAdapter : ListAdapter<DailyForecast, RecyclerView.ViewHolder>(DIFFER) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ForecastViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindItem(holder as ForecastViewHolder, position)
    }

    private fun bindItem(holder: ForecastViewHolder, position: Int) {
        val item = getItem(position) as DailyForecast
        with(holder.viewBinding) {
            day.text = setDateFormat(item.date, true)
            labelDate.text = setDateFormat(item.date)
            temperatureHigh.text = item.temperature.maximum.value.toString()
            setTextColorBasedOnTempValue(temperatureHigh, item.temperature.maximum.value)
            temperatureLow.text = item.temperature.minimum.value.toString()
            setTextColorBasedOnTempValue(temperatureLow, item.temperature.minimum.value)
            description.text = item.day.iconPhrase
            weatherImage.setImageResource(setWeatherIcon(item.day.iconPhrase))
        }
    }

    private fun setTextColorBasedOnTempValue(textField: TextView, temp: Float) {
        when {
            temp >= 20f -> textField.setTextColor(Color.RED)
            temp >= 10f && temp < 20f -> textField.setTextColor(Color.BLACK)
            temp < 10f -> textField.setTextColor(Color.BLUE)
        }
    }

    private fun setWeatherIcon(iconPhrase: String): Int {
        return when (iconPhrase) {
            "Sunny", "Mostly sunny", "Partly Sunny", "Intermittent Clouds", "Hazy Sunshine", "Hot", "Clear" -> R.drawable.ic_baseline_wb_sunny_24
            "Mostly Cloudy", "Cloudy", "Dreary (Overcast)", "Fog", "Flurries", "Mostly Cloudy w/ Flurries", "Partly Sunny w/ Flurries" -> R.drawable.ic_baseline_wb_cloudy_24
            "Showers", "Mostly Cloudy w/ Showers", "Partly Sunny w/ Showers", "T-Storms", "Mostly Cloudy w/ T-Storms", "Partly Sunny w/ T-Storms", "Rain" -> R.drawable.ic_baseline_grain_24
            "Mostly Cloudy w/ Snow", "Ice", "Sleet", "Freezing Rain", "Rain and Snow", "Cold" -> R.drawable.ic_baseline_frosty
            "Windy" -> R.drawable.ic_baseline_windy
            else -> R.drawable.ic_baseline_wb_sunny_24
        }
    }

    @Throws(ParseException::class)
    fun setDateFormat(unformattedDate: String?, dayName: Boolean = false): String? {
        val dateFormat: Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(unformattedDate)
        return when (dayName) {
            false -> SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateFormat)
            true -> SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateFormat)
        }

    }

    companion object {
        private val DIFFER = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean = oldItem.date == newItem.date
        }
    }

    class ForecastViewHolder(val viewBinding: ListWeatherForecastItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        companion object {
            fun newInstance(parent: ViewGroup): ForecastViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListWeatherForecastItemBinding.inflate(inflater, parent, false)
                return ForecastViewHolder(binding)
            }
        }
    }
}
