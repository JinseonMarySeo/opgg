package com.maryseo.opgg_test.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryseo.opgg_test.network.data.dto.Game
import com.maryseo.opgg_test.network.data.dto.Summoner
import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.other.ApiState
import com.maryseo.opgg_test.network.repository.MainRepository
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _summoner: MutableStateFlow<ApiState<Summoner>> = MutableStateFlow(ApiState.Loading())
    val summoner: StateFlow<ApiState<Summoner>> = _summoner

    private val _matches: MutableStateFlow<ApiState<MatchesResponse>> = MutableStateFlow(ApiState.Loading())
    val matches: StateFlow<ApiState<MatchesResponse>> = _matches

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

    fun getSummoner(name: String, errorMessage: String) {
        viewModelScope.launch {
            _summoner.value = ApiState.Loading()
            repository.getSummoner(name)
                .suspendOnSuccess {
                    _summoner.emit(ApiState.Success(data.summoner))
                }.onFailure {
                    _summoner.value = ApiState.Error(errorMessage)
                }
        }
    }

    fun getMatches(name: String, lastMatch: Long? = null, errorMessage: String) {
        viewModelScope.launch {
            _matches.value = ApiState.Loading()
            repository.getMatches(name, lastMatch)
                .suspendOnSuccess {
                    if (lastMatch == null) {
                        _latestMatches.postValue(data)
                        initGameList()
                    }
                    _matches.value = ApiState.Success(data)
                    addGameList(matches.value.data?.games)
                }.onFailure { _matches.value = ApiState.Error(errorMessage) }
        }
    }
}