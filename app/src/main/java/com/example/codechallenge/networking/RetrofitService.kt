package com.example.codechallenge.networking

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitService {

    @GET(EndPointConstants.GET_ALL_CURRENCIES)
    suspend fun getAllCurrencies(): Response<ResponseBody>

    @GET(EndPointConstants.GET_LATEST_RATES)
    suspend fun getLatestRates(
        @Query("app_id") app_id: String = "0fe344860f8e4aa3b5a902fbd4fb273d",
    ): Response<ResponseBody>

}