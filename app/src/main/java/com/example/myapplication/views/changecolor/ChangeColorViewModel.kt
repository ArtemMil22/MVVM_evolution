package com.example.myapplication.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.foundation.model.PendingResult
import com.example.foundation.model.Result
import com.example.foundation.model.SuccessResult
import com.example.foundation.sideeffects.navigator.Navigator
import com.example.foundation.sideeffects.resources.Resources
import com.example.foundation.sideeffects.toasts.Toasts
import com.example.foundation.views.BaseViewModel
import com.example.myapplication.R
import com.example.myapplication.model.colors.NamedColor
import com.example.myapplication.views.changecolor.ChangeColorFragment.Screen
import com.example.simplemvvm.model.colors.ColorsRepository
import kotlinx.coroutines.CancellationException
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
    private val _availableColors = MutableStateFlow<Result<List<NamedColor>>>(PendingResult())
    private val _currentColorId =
        savedStateHandle.getMyStateFlow("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableStateFlow(false)

    // Объеденяем значения stateFlow
    val viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _saveInProgress,
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
        if (_saveInProgress.value == true) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {
            _saveInProgress.value = true
            val currentColorId = _currentColorId.value
                ?: throw IllegalStateException("Color ID should not be NULL")
            val currentColor = colorsRepository.getById(currentColorId)
            colorsRepository.setCurrentColor(currentColor).collect()
            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) toasts.toast(resources.getString(R.string.error_happened))
        } finally {
            _saveInProgress.value = false
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
        saveInProgress: Boolean,
    ): Result<ViewState> {

        // map Result<List<NamedColor>> to Result<ViewState>
        return colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },

                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress
            )
        }
    }

    private fun load() = into(_availableColors) { colorsRepository.getAvailableColors() }


}