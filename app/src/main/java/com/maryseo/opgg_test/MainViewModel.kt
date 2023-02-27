package com.maryseo.opgg_test

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maryseo.opgg_test.data.Summoner
import com.maryseo.opgg_test.network.ApiClient
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val service by lazy { ApiClient.service }

    val loading = MutableLiveData<Boolean>()
    val summoner = MutableLiveData<Summoner>()

    fun getSummoner(name: String, summoner: MutableState<Summoner>) {
        loading.postValue(true)
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val response = service.getSummoner(name)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.summoner?.let { summoner.value = it }
//                        summoner.value = response.body()?.summoner
//                        summoner = response.body()?.summoner
                    }
                    loading.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
            }
        }

    }
}