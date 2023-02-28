package com.maryseo.opgg_test.network.api

import com.maryseo.opgg_test.network.model.MatchesResponse
import com.maryseo.opgg_test.network.model.SummonerResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val service: ApiService) : ApiHelper {
    override suspend fun getSummary(name: String): Response<SummonerResponse> = service.getSummoner(name)

    override suspend fun getMatches(name: String, lastMatch: Long): Response<MatchesResponse> = service.getMatches(name, lastMatch)
}