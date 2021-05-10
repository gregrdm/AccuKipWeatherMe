package com.gregrdm.accukipweatherme.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gregrdm.accukipweatherme.R
import com.gregrdm.accukipweatherme.api.model.City
import com.gregrdm.accukipweatherme.databinding.MainFragmentBinding
import com.gregrdm.accukipweatherme.ui.main.adapter.CityListRecyclerViewAdapter
import com.gregrdm.accukipweatherme.ui.utils.bindData

class MainFragment : Fragment() {

    lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    var cityListAdapter = CityListRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = bindData<MainFragmentBinding>(inflater, container, R.layout.main_fragment)
            .apply {
                adapter = cityListAdapter
                lifecycleOwner = this@MainFragment.viewLifecycleOwner
                vm = viewModel
            }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addCityInputListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            screenState.observe(this@MainFragment.viewLifecycleOwner, {
                when (it) {
                    is MainViewModel.ScreenState.ShowData -> submitList(it.data)
                    MainViewModel.ScreenState.NoData -> submitList(emptyList())
                    is MainViewModel.ScreenState.ShowError -> showFailureMessage(it.errorMessage)
                }
            })
        }
        cityListAdapter.apply {
            onItemClick = { } // Move to next fragment, City
         }
    }

    private fun addCityInputListener() {
        binding.inputCityName.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.fetchCities(binding.inputCityName.editText?.text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun submitList(list: List<City>) = cityListAdapter.submitList(list)

    private fun showFailureMessage(message: String) = Toast.makeText(context, "Something went wrong $message", Toast.LENGTH_SHORT).show()

    companion object {
        fun newInstance() = MainFragment()
    }
}
