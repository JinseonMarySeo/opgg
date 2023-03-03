package com.maryseo.opgg_test.network.other

sealed class ApiState<T>(
    val state: State,
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ApiState<T>(State.SUCCESS, data)
    class Error<T>(message: String, data: T? = null) : ApiState<T>(State.ERROR, data, message)
    class Loading<T> : ApiState<T>(State.LOADING)
}

enum class State {
    LOADING,
    SUCCESS,
    ERROR
}