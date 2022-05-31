package com.entin.cataas.presentation.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entin.cataas.presentation.utils.Fail
import com.entin.cataas.presentation.utils.Pending
import com.entin.cataas.presentation.utils.Success
import com.entin.cataas.presentation.utils.ViewResult
import com.entin.domain.dto.SearchCatFullRequest
import com.entin.domain.dto.SearchCatShortRequest
import com.entin.domain.model.CatDomain
import com.entin.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainScreen ViewModel for [MainScreenFragment]
 * Gets data from [Repository] and prepare MainScreen ViewState to show.
 */

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    var allTags = mutableListOf<String>()
        private set
    private val dispatcher = Dispatchers.IO

    private val _stateSplashScreen = MutableStateFlow(true)
    val stateSplashScreen = _stateSplashScreen.asStateFlow()

    private val _stateMainScreen = MutableLiveData<ViewResult<MainScreenViewState>>()
    val stateMainScreen: LiveData<ViewResult<MainScreenViewState>> = _stateMainScreen

    fun getRandomCat() = viewModelScope.launch(dispatcher) {
        _stateMainScreen.postValue(Pending())

        repository.getRandomCat().collect { result ->
            decision(result)
        }
    }

    fun searchFull(searchCatRequest: SearchCatFullRequest) = viewModelScope.launch(dispatcher) {
        _stateMainScreen.postValue(Pending())

        repository.getFullSearchCat(searchCatRequest).collect { result ->
            decision(result)
        }
    }

    fun searchShort(searchCatRequest: SearchCatShortRequest) = viewModelScope.launch(dispatcher) {
        _stateMainScreen.postValue(Pending())

        repository.getShortSearchCat(searchCatRequest).collect { result ->
            decision(result)
        }
    }

    fun fetchTags() = viewModelScope.launch(dispatcher) {
        repository.getAllTags().collect { result ->
            result.onSuccess { tags ->
                allTags.addAll(tags)
            }
            _stateSplashScreen.value = false
        }
    }

    private fun decision(result: Result<CatDomain>) {
        result.onSuccess { cat ->
            _stateMainScreen.postValue(Success(data = MainScreenViewState(catImgUrl = cat.urlImg)))
        }.onFailure {
            _stateMainScreen.postValue(Fail())
        }
    }
}
