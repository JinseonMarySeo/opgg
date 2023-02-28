package com.maryseo.opgg_test.network.model

data class MatchesResponse(
    val games: List<Game>,
    val champions: List<Champion>,
    val positions: List<Position>,
    val summary: Summary
)