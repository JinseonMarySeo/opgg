package com.maryseo.opgg_test.network.data.response

import android.util.Log
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
        val t = champions.sortedByDescending { it.wins.toFloat() / it.games }.take(count)
        champions.forEachIndexed { index, champion ->
            Log.e("MatchesResponse", "champions[$index] = ${champion.name}")
        }
        return t
    }

    fun getMostPosition(): Position {
//        val t = positions.sortedByDescending { it.wins.toFloat() / it.games }.take(1)
        return positions.sortedByDescending { it.wins.toFloat() / it.games }.take(1)[0]
    }

//    val mostChampions: List<Champion> = champions.sortedByDescending { it.wins.toFloat() / it.games }.take(2)
//    val mostPosition = positions.sortedByDescending { it.wins.toFloat() / it.games }.take(1)
}