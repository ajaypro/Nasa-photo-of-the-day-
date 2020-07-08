package com.deepak.nasa.data.network

sealed class Resource<out T> {

        object Loading : Resource<Nothing>()
        object NetworkError : Resource<Nothing>()
        data class Error<T>(val message: String) : Resource<T>()
        data class Success<T>(val data: T?) : Resource<T>()

}