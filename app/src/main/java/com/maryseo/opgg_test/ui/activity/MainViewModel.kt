package com.maryseo.opgg_test.ui.activity

import androidx.lifecycle.*
import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.data.dto.Summoner
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
                    _matches.postValue(Result.success(it.body()))
                } else {
                    _matches.postValue(Result.fail(it.errorBody().toString()))
                }
            }
        }
    }
}