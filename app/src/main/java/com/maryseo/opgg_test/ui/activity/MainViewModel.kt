package com.maryseo.opgg_test.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryseo.opgg_test.network.data.dto.Game
import com.maryseo.opgg_test.network.data.dto.Summoner
import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.other.Result
import com.maryseo.opgg_test.network.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _summoner = MutableLiveData<Result<Summoner>>()
    val summoner: LiveData<Result<Summoner>> = _summoner

    private val _matches = MutableLiveData<Result<MatchesResponse>>()
    val matches: LiveData<Result<MatchesResponse>> = _matches

    private val _latestMatches = MutableLiveData<MatchesResponse>()
    val latestMatch: LiveData<MatchesResponse> = _latestMatches

    private val _gameList = MutableLiveData<ArrayList<Game>>()
    val gameList: LiveData<ArrayList<Game>> = _gameList


    init {
        _gameList.value = ArrayList()
    }

    private fun initGameList() {
        _gameList.value?.clear()
    }

    private fun addGameList(list: List<Game>?) {
        list?.let {  _gameList.value?.addAll(list) }
    }

    fun getSummoner(name: String) {
        viewModelScope.launch {
            _summoner.postValue(Result.loading())
            repository.getSummoner(name).let {
                if (it.isSuccessful) {
                    _summoner.postValue(Result.success(it.body()?.summoner))
                } else {
                    _summoner.postValue(Result.fail(it.errorBody().toString()))
                }
            }
        }
    }

    fun getMatches(name: String, createDate: Long? = null) {
        viewModelScope.launch {
            _matches.postValue(Result.loading())
            repository.getMatches(name, createDate).let {
                if (it.isSuccessful) {
                    if (createDate == null) {
                        _latestMatches.postValue(it.body())
                        initGameList()
                    }
                    _matches.postValue(Result.success(it.body()))
                    addGameList(Result.success(it.body()).data?.games)
                } else {
                    _matches.postValue(Result.fail(it.errorBody().toString()))
                }
            }
        }
    }
}