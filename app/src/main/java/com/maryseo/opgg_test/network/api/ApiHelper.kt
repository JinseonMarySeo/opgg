package com.maryseo.opgg_test.network.api

import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.data.response.SummonerResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getSummary(name: String): Response<SummonerResponse>

    suspend fun getMatches(name: String, lastMatch: Long?): Response<MatchesResponse>
}