package com.naufal.kidsnesia.auth.data.source.remote.network

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error<out T>(val errorMessage: String) : ApiResponse<T>() // Tambahkan out T
    object Empty : ApiResponse<Nothing>()
}
