package com.example.codechallenge.networking

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(private val dataSource: DataSource) : BaseApiResponse() {

    suspend fun getAllCurrencies(): Flow<NetworkResult<ResponseBody>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { dataSource.getAllCurrencies() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLatestRates(): Flow<NetworkResult<ResponseBody>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { dataSource.getLatestRates() })
        }.flowOn(Dispatchers.IO)
    }
}