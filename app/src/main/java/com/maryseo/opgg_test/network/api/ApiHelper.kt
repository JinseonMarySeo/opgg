package com.maryseo.opgg_test.network.api

import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.data.response.SummonerResponse
import com.skydoves.sandwich.ApiResponse

interface ApiHelper {
    suspend fun getSummary(name: String): ApiResponse<SummonerResponse>

    suspend fun getMatches(name: String, lastMatch: Long?): ApiResponse<MatchesResponse>
}