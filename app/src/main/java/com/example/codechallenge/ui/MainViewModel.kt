package com.example.codechallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.interfaces.DataListener
import com.example.codechallenge.networking.NetworkResult
import com.example.codechallenge.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var repository: Repository) : ViewModel() {

    lateinit var dataListener: DataListener

    fun setListener(dataListener: DataListener) {
        this.dataListener = dataListener
    }

    fun getAllCurrencies() = viewModelScope.launch {
        repository.getAllCurrencies().collect { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val data = response.data?.string()
                    data?.let { dataListener.onCurrenciesData(it) }
                }

                else -> {

                }
            }
        }
    }

    fun getLatestRates() = viewModelScope.launch {
        repository.getLatestRates().collect { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val data = response.data?.string()
                    data?.let { dataListener.onRatesData(it) }
                }

                else -> {

                }
            }
        }
    }
}