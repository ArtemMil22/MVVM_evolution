package com.example.myapplication.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.foundation.model.*
import com.example.foundation.sideeffects.navigator.Navigator
import com.example.foundation.sideeffects.resources.Resources
import com.example.foundation.sideeffects.toasts.Toasts
import com.example.foundation.utils.finiteShareIn
import com.example.foundation.views.BaseViewModel
import com.example.myapplication.R
import com.example.myapplication.model.colors.NamedColor
import com.example.myapplication.views.changecolor.ChangeColorFragment.Screen
import com.example.simplemvvm.model.colors.ColorsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors =
        MutableStateFlow<Result<List<NamedColor>>>(PendingResult())
    private val _currentColorId =
        savedStateHandle.getMyStateFlow(
            "currentColorId", screen.currentColorId
        )
    // прогресс бар между экрана (значения приходят все время)
    private val _instantSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)
    // Обновление прогрессов с разной частотой,
    // внизу будет обновляться чуть реже, тот,
    // что будет передавать значение текстовой надписи 100%
    // (значения приходят с некоторой задержкой)
    private val _sampledSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)

    // Объеденяем значения stateFlow
    val viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _instantSaveInProgress,
        _sampledSaveInProgress,
        ::mergeSources
    )

    val screenTitle: LiveData<String> = viewState
        .map { result ->
            return@map if (result is SuccessResult) {
                val currentColor = result.data.colorsList.first { it.selected }
                resources.getString(
                    R.string.change_color_screen_title,
                    currentColor.namedColor.name
                )
            } else {
                resources.getString(R.string.change_color_screen_title_simple)
            }
        }.asLiveData()

    init {
        load()
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_instantSaveInProgress.value.isInProgress()) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {
            _instantSaveInProgress.value = PercentageProgress.START
            _sampledSaveInProgress.value = PercentageProgress.START

            val currentColorId = _currentColorId.value
            val currentColor = colorsRepository.getById(currentColorId)

            val flow = colorsRepository.setCurrentColor(currentColor)
                .finiteShareIn(this)

            val instantJob = async {
                flow.collect { percentage ->
                    _instantSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            val sampledJob = async {
                flow.sample(200).collect { percentage ->
                    _sampledSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            instantJob.await()
            sampledJob.await()

            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) toasts.toast(resources.getString(R.string.error_happened))
        } finally {
            _instantSaveInProgress.value = EmptyProgress
            _sampledSaveInProgress.value = EmptyProgress
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        load()
    }

    private fun mergeSources(
        colors: Result<List<NamedColor>>,
        currentColorId: Long,
        instantInProgress: Progress,
        sampledSaveInProgress: Progress
    ): Result<ViewState> {

        // map Result<List<NamedColor>> to Result<ViewState>
        return colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map {
                    NamedColorListItem(it, currentColorId == it.id)
                                            },
                showSaveButton = !instantInProgress.isInProgress(),
                showCancelButton = !instantInProgress.isInProgress(),
                showSaveProgressBar = instantInProgress.isInProgress(),

                saveProgressPercentage = instantInProgress.getPercentage(),
                saveProgressPercentageMessage = resources.getString(
                    R.string.percentage_value,
                    sampledSaveInProgress.getPercentage()
                )

            )
        }
    }

    private fun load() = into(_availableColors) { colorsRepository.getAvailableColors() }


}