package com.example.newstest.ExtraPackage

import com.bumptech.glide.load.engine.Resource

sealed class ErrorHandling<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ErrorHandling<T>(data)
    class Error<T>(message: String, data: T? = null) : ErrorHandling<T>(data, message)

}