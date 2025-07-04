package com.example.spoti5.utils

sealed class Resource <out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String?) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()


}