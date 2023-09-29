package com.example.imovies.data.repository

import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.data.repository.common.response.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

abstract class HandleResponse {

    companion object {
        const val GENERAL_ERROR_CODE = 500
        private const val UNAUTHORIZED = "Unauthorized"
        private const val BAD_REQUEST = "Bad Request"
        private const val NOT_FOUND = "Not found"
        private const val SOMETHING_WRONG = "Something went wrong"

        fun <T : Any> success(data: T): Resource<T> {
            return Resource.Success(data)
        }

        fun <T : Any> exception(
            code: Int
        ): Resource<T> {
            return Resource.Error(Exception(getErrorMessage(code)))
        }

        fun <T : Any> exception(
            code: Int,
            res: ResponseBody? = null
        ): Resource<T> {
            val gson = Gson()
            val type = object : TypeToken<ErrorResponse>() {}.type
            val errorResponse: ErrorResponse? = gson.fromJson(res?.charStream(), type)
            val exception = errorResponse?.errorMessage ?: getErrorMessage(code)
            return Resource.Error(Exception(exception))
        }

        fun <T : Any> exception(
            code: Int,
            message: String? = null
        ): Resource<T> {
            val exception = message ?: getErrorMessage(code)
            return Resource.Error(Exception(exception))
        }

        private fun getErrorMessage(httpCode: Int): String {
            return when (httpCode) {
                400 -> BAD_REQUEST
                401 -> UNAUTHORIZED
                404 -> NOT_FOUND
                else -> SOMETHING_WRONG
            }
        }
    }
}