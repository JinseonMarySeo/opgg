package com.maryseo.opgg_test.network.api

import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.data.response.SummonerResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("summoner/{name}")
    suspend fun getSummoner(
        @Path("name") name: String
    ): ApiResponse<SummonerResponse>

    @GET("summoner/{name}/matches")
    suspend fun getMatches(
        @Path("name") name: String,
        @Query("lastMatch") lastMatch: Long? = System.currentTimeMillis()
    ): ApiResponse<MatchesResponse>
}