package com.example.myapplication.views.changecolor

import androidx.lifecycle.*
import com.example.foundation.model.PendingResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MediatorLiveResult
import com.example.foundation.views.MutableLiveResult
import com.example.myapplication.R
import com.example.myapplication.model.colors.NamedColor
import com.example.myapplication.views.changecolor.ChangeColorFragment.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.cn.stu.simplemvvm.model.colors.ColorsRepository

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    // 1ый и 2ой источник данных
    // галочка текущего цвета
    // для кнопки save
    private val _availableColors = MutableLiveResult<List<NamedColor>>(PendingResult())
    private val _currentColorId =
        savedStateHandle.getLiveData("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableLiveData(false)

    // объеденение двух результатов одну LD
    private val _viewState = MediatorLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState

    // обновление screenTitle, формирование заголовка экрана так сказать
    val screenTitle: LiveData<String> = Transformations.map(viewState) { result ->
        // находим текущий цвет
        if (result is SuccessResult) {
            val currentColor = result.data.colorsList.first { it.selected }
            uiActions.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
        } else {
            uiActions.getString(R.string.change_color_screen_title_simple)
        }
    }

    private var mockErrror = true

    init {
        viewModelScope.launch {

            delay(2000)
            _availableColors.value = SuccessResult(colorsRepository.getAvailableColors())
                // _availableColors.value = ErrorResult(RuntimeException())
        }
        // initializing MediatorLiveData
        _viewState.addSource(_availableColors) { mergeSources() }
        _viewState.addSource(_currentColorId) { mergeSources() }
        _viewState.addSource(_saveInProgress) { mergeSources() }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        //добавить запрет на нажатие кнопки в момент прогресса
        if (_saveInProgress.value == true) return

        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() {
        viewModelScope.launch {
            _saveInProgress.postValue(true)
            delay(1000)
            if (mockErrror) {
                _saveInProgress.postValue(false)
                uiActions.toast(uiActions.getString(R.string.error_happened))
                mockErrror = false
            } else {
                val currentColorId = _currentColorId.value ?: return@launch
                val currentColor = colorsRepository.getById(currentColorId)
                colorsRepository.currentColor = currentColor
                navigator.goBack(result = currentColor)
            }
        }
    }
    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        viewModelScope.launch {
            _availableColors.postValue(PendingResult())
            delay(2000)
            _availableColors.postValue(
                SuccessResult(colorsRepository.getAvailableColors())
            )
        }
    }

    /**
     * [MediatorLiveData] can listen other LiveData instances (even more than 1)
     * and combine their values.
     * Here we listen the list of available colors ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data), then we use both of these values in order to create a list of
     * [NamedColorListItem], it is a list to be displayed in RecyclerView.
     */

    // два источника данныз 1ый идентификатор из скрина, что галочкой отмечаем
    // 2ой с репозитория список всех доступных цветов, тут происходит объеденение
    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val saveInProgress = _saveInProgress.value ?: return

        _viewState.value = colors.map { colorsList ->
            ViewState(
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },
                showCancelButton = !saveInProgress,
                showSaveButton = !saveInProgress,
                showSaveProgressBar = saveInProgress
            )
        }
    }
}
// val currentColor = colors.first { it.id == currentColorId }
// _screenTitle.value = uiActions.getString(R.string.change_color_screen_title, currentColor.name)

