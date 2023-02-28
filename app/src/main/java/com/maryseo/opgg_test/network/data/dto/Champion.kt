package com.maryseo.opgg_test.network.data.dto

data class Champion(
    val assists: Int,
    val deaths: Int,
    val games: Int,
    val id: Int,
    val imageUrl: String,
    val key: String,
    val kills: Int,
    val losses: Int,
    val name: String,
    val wins: Int
) {
    fun getRate() = (wins.toFloat() / games * 100).toInt()
}