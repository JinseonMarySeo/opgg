package com.maryseo.opgg_test.network

import com.maryseo.opgg_test.data.SummonerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("summoner/{name}")
    suspend fun getSummoner(@Path("name") name: String): Response<SummonerResponse?>
}