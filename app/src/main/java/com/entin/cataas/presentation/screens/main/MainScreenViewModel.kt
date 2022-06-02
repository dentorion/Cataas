package com.entin.cataas.presentation.screens.main

import androidx.lifecycle.*
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
import kotlin.random.Random

/**
 * MainScreen ViewModel for [MainScreenFragment]
 * Gets data from [Repository] and prepare MainScreen ViewState to show.
 */

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository,
    menuService: MenuService,
) : ViewModel() {

    companion object {
        const val TAG = "tag"
        const val FILTER = "filter"
        const val FULL_REQUEST = "filter"
        const val TEXT_LABEL = "text"
        const val SLIDER = "slider"
        const val COLOR = "color"

        const val RANDOM = "Losowy"
        const val DEFAULT_TEXT_LABEL = "Hello, Kitty!"
        const val ZERO_VALUE = 0
    }

    var isSuccessfulStart: Boolean = false

    var allTags = mutableListOf<String>()
        private set
    val filters = menuService.getFilterMenuItems()
    val colors = menuService.getColorMenuItems()

    var isFullSearch = state.get<Boolean>(FULL_REQUEST) ?: false
        set(value) {
            field = value
            state.set(FULL_REQUEST, value)
        }

    var chosenTag = state.get<Int>(TAG) ?: ZERO_VALUE
        set(value) {
            field = value
            state.set(TAG, value)
        }

    var chosenFilter = state.get<Int>(FILTER) ?: ZERO_VALUE
        set(value) {
            field = value
            state.set(FILTER, value)
        }

    var textLabelValue = state.get<String>(TEXT_LABEL) ?: DEFAULT_TEXT_LABEL
        set(value) {
            field = value
            state.set(TEXT_LABEL, value)
        }

    var sliderValue = state.get<Int>(SLIDER) ?: ZERO_VALUE
        set(value) {
            field = value
            state.set(SLIDER, value)
        }

    var chosenColor = state.get<Int>(COLOR) ?: ZERO_VALUE
        set(value) {
            field = value
            state.set(COLOR, value)
        }

    private val dispatcher = Dispatchers.IO

    private val _stateSplashScreen = MutableStateFlow(true)
    val stateSplashScreen = _stateSplashScreen.asStateFlow()

    private val _stateMainScreen = MutableLiveData<ViewResult<MainScreenViewState>>()
    val stateMainScreen: LiveData<ViewResult<MainScreenViewState>> = _stateMainScreen

    fun fetchTags() = viewModelScope.launch(dispatcher) {
        if (allTags.isEmpty()) {
            repository.getAllTags().collect { result ->
                result.onSuccess { tags ->
                    allTags.addAll(tags)
                    isSuccessfulStart = true
                    allTags[ZERO_VALUE] = RANDOM
                }.onFailure {
                    allTags.add(RANDOM)
                }
                _stateSplashScreen.value = false
            }
        }
    }

    fun getRandomCat() = viewModelScope.launch(dispatcher) {
        _stateMainScreen.postValue(Pending())

        repository.getRandomCat().collect { result ->
            decision(result)
        }
    }

    fun search(isFull: Boolean) {
        if (isSuccessfulStart) {
            if (isFull) searchFull(generateFullRequest())
            else searchShort(generateShortRequest())
        } else {
            _stateMainScreen.postValue(Fail())
        }
    }

    private fun generateFullRequest() =
        SearchCatFullRequest(
            tag = checkForRandomChoice(allTags[chosenTag]),
            filter = filters[chosenFilter],
            text = textLabelValue,
            size = sliderValue.toString(),
            colors = colors[chosenColor]
        )

    private fun generateShortRequest() =
        SearchCatShortRequest(
            tag = checkForRandomChoice(allTags[chosenTag]),
            filter = filters[chosenFilter],
        )

    private fun checkForRandomChoice(check: String): String =
        if (check == RANDOM) allTags[Random.nextInt(1, allTags.size - 1)]
        else check

    private fun searchFull(searchCatRequest: SearchCatFullRequest) =
        viewModelScope.launch(dispatcher) {
            _stateMainScreen.postValue(Pending())

            repository.getFullSearchCat(searchCatRequest).collect { result ->
                decision(result)
            }
        }

    private fun searchShort(searchCatRequest: SearchCatShortRequest) =
        viewModelScope.launch(dispatcher) {
            _stateMainScreen.postValue(Pending())

            repository.getShortSearchCat(searchCatRequest).collect { result ->
                decision(result)
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
