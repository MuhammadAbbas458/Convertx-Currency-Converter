package com.example.codechallenge.networking

import android.util.Log
import okhttp3.Headers
import org.json.JSONObject
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            Log.wtf("BaseApiResponse", "${apiCall.javaClass} ApiCall")
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                val headers: Headers = response.headers()
                body?.let {
                    Log.wtf("BaseApiResponse", "${apiCall.javaClass} Success : ${response.message()}")
                    return NetworkResult.Success(body, headers = headers, message = response.message())
                }
            }
            if (response.errorBody() != null) {
                var message = "Something went wrong. Please try again later"
                val errorCode = response.raw().code
                val jObjError = JSONObject(response.errorBody()!!.string())
                try {
                    message = jObjError.getString("message")
                } catch (_: Exception) {

                }
//                Toast.makeText(getContext(), jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show()
                Log.wtf("BaseApiResponse", "${apiCall.javaClass} Error : ${response.errorBody().toString()}")
                return NetworkResult.Failure(message = message, errorCode = errorCode)
            }
            return NetworkResult.Failure(message = "${response.code()} ${response.message()}", errorCode = response.code())
        } catch (e: Exception) {
            Log.wtf("BaseApiResponse", "${apiCall.javaClass} ${e.javaClass} ${e.message}")
            return when (e.javaClass) {
                UnknownHostException().javaClass, java.net.SocketException().javaClass, NoConnectivityException().javaClass -> {
                    NetworkResult.Error(e.message, Errors.NetworkError(e))
                }

                java.net.ConnectException().javaClass, java.io.IOException().javaClass -> {
                    NetworkResult.Error(e.message, Errors.ServerError(e))
                }

                InterruptedIOException().javaClass, SocketTimeoutException().javaClass -> {
                    NetworkResult.Error(e.message, Errors.TimeOutError(e))
                }

                else -> {
                    NetworkResult.Error(e.message, Errors.Error(e))
                }
            }
        }
    }
}