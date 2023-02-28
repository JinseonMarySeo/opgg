package com.maryseo.opgg_test.network.model

data class League(
    val hasResult: Boolean,
    val wins: Int,
    val losses: Int,
    val tierRank: Tier
)
