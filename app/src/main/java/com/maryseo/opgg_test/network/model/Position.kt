package com.maryseo.opgg_test.network.model

data class Position(
    val games: Int,
    val losses: Int,
    val position: String,
    val positionName: String,
    val wins: Int
)