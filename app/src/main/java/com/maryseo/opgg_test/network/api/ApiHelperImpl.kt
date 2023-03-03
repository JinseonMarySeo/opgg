package com.maryseo.opgg_test.network.api

import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.data.response.SummonerResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val service: ApiService) : ApiHelper {
    override suspend fun getSummary(name: String): ApiResponse<SummonerResponse> = service.getSummoner(name)

    override suspend fun getMatches(name: String, lastMatch: Long?): ApiResponse<MatchesResponse> = service.getMatches(name, lastMatch)
}