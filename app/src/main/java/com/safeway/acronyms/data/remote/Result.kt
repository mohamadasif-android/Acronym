package com.safeway.acronyms.data.remote

import androidx.annotation.StringRes

data class Result<out T>(val status: Status, val data: T?, @StringRes val messageResId: Int?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {

        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(msgResId: Int, data: T? = null): Result<T> {
            return Result(Status.ERROR, data, msgResId)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, data, null)
        }
    }
}



