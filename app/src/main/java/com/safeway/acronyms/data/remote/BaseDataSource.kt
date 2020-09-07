package com.safeway.acronyms.data.remote

import androidx.annotation.StringRes
import com.safeway.acronyms.R
import com.safeway.acronyms.data.remote.interceptors.NoConnectionInterceptor.NoConnectivityException
import com.safeway.acronyms.data.remote.interceptors.NoConnectionInterceptor.NoInternetException
import retrofit2.Response
import timber.log.Timber

/**
 * Abstract Base Data source class with error handling
 */

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.success(response.body())
            }
        }
        Timber.e("${response.code()} ${response.message()}")
        return error("${response.code()}")
    } catch (exception: Exception) {
        val msgResId = when (exception) {
            is NoInternetException -> R.string.error_no_internet
            is NoConnectivityException -> R.string.error_no_network
            else -> R.string.error_unknown
        }
        return error(msgResId)
    }
}

private fun <T> error(@StringRes messageResID: Int): Result<T> {
    return Result.error(msgResId = messageResID)
}




