package com.maryseo.opgg_test.network.repository

import com.maryseo.opgg_test.network.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper){
    suspend fun getSummoner(name: String) = apiHelper.getSummary(name)

    suspend fun getMatches(name: String, lastMatch: Long?) = apiHelper.getMatches(name, lastMatch)
}