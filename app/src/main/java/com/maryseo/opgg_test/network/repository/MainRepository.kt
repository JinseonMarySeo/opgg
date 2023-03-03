package com.maryseo.opgg_test.network.repository

import com.maryseo.opgg_test.network.api.ApiHelper
import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.data.response.SummonerResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getSummoner(name: String): ApiResponse<SummonerResponse> = apiHelper.getSummary(name)

    suspend fun getMatches(name: String, lastMatch: Long?): ApiResponse<MatchesResponse> = apiHelper.getMatches(name, lastMatch)
}