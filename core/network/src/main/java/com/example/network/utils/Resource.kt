package com.example.network.utils

sealed class Resource<out T> {

    data class Success<out T>(val value: T) : Resource<T>()

    class Failure(val exception: Throwable) : Resource<Nothing>()

    companion object {
        fun  error (exception: Throwable)  = Failure(exception)
        fun <T> success(data: T) = Success(data)
    }

    fun isSuccess () = this is Success

    fun getSuccessData() = (this as Success).value

    fun getErrorMessage ()= if (this is Failure) exception.localizedMessage else null

}