package com.maryseo.opgg_test.network.data.response

import com.maryseo.opgg_test.network.data.dto.Champion
import com.maryseo.opgg_test.network.data.dto.Game
import com.maryseo.opgg_test.network.data.dto.Position
import com.maryseo.opgg_test.network.data.dto.Summary

data class MatchesResponse(
    val games: List<Game>,
    val champions: List<Champion>,
    val positions: List<Position>,
    val summary: Summary
) {
    fun getMostChampions(): List<Champion> {
        val count = if (champions.size < 2) champions.size else 2
        return champions.sortedByDescending { it.wins.toFloat() / it.games }.take(count)
    }

    fun getMostPosition(): Position {
        return positions.sortedByDescending { it.wins.toFloat() / it.games }.take(1)[0]
    }
}