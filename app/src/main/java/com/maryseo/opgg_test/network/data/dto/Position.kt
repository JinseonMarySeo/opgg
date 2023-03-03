package com.maryseo.opgg_test.network.data.dto

data class Position(
    val games: Int,
    val losses: Int,
    val position: String,
    val positionName: String,
    val wins: Int
) {
    fun getRate() = (wins.toFloat() / games * 100).toInt()
}