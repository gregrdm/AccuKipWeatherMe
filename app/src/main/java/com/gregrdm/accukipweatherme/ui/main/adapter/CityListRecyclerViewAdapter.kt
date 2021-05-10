package com.gregrdm.accukipweatherme.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gregrdm.accukipweatherme.api.model.City
import com.gregrdm.accukipweatherme.databinding.ListWeatherCityItemBinding

class CityListRecyclerViewAdapter : ListAdapter<City, RecyclerView.ViewHolder>(DIFFER) {

    var onItemClick: (City) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CityViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindCityItem(holder as CityViewHolder, position)
    }

    private fun bindCityItem(holder: CityViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.viewBinding) {
            cityName = item.localizedName
            cityDetails = "${item.region.englishName}, ${item.country.englishName}"
        }
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    companion object {
        private val DIFFER = object : DiffUtil.ItemCallback<City>() {
            override fun areItemsTheSame(oldItem: City, newItem: City): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: City, newItem: City): Boolean = oldItem.geoPosition == newItem.geoPosition
        }
    }

    class CityViewHolder(val viewBinding: ListWeatherCityItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        companion object {
            fun newInstance(parent: ViewGroup): CityViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListWeatherCityItemBinding.inflate(inflater, parent, false)
                return CityViewHolder(binding)
            }
        }
    }
}
