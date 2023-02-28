package com.maryseo.opgg_test.network.other

data class Result<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, data, null)
        }

        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> fail(msg: String, data: T? = null): Result<T> {
            return Result(Status.FAIL, data, msg)
        }
    }
}

enum class Status {
    LOADING,
    SUCCESS,
    FAIL
}