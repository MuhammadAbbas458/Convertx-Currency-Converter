package com.example.codechallenge.networking

import javax.inject.Inject

class DataSource @Inject constructor(private val retrofitService: RetrofitService) {

    suspend fun getAllCurrencies() = retrofitService.getAllCurrencies()

    suspend fun getLatestRates() = retrofitService.getLatestRates()

}