package com.example.myapplication.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.FinalResult
import com.example.foundation.model.PendingResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.task.TasksFactory
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MediatorLiveResult
import com.example.foundation.views.MutableLiveResult
import com.example.myapplication.R
import com.example.myapplication.model.colors.NamedColor
import com.example.myapplication.views.changecolor.ChangeColorFragment.Screen
import ua.cn.stu.simplemvvm.model.colors.ColorsRepository

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    private val tasksFactory: TasksFactory,
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

    init {
        load()

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
        // показать прогресс
        _saveInProgress.postValue(true)

        tasksFactory.async {
            //берем текущий индетификатор хранящийся в LD,
            // код синхронный, который вызывается асинхронно
            val currentColorId = _currentColorId.value ?: throw IllegalStateException("Color ID should not be NULL")
            val currentColor = colorsRepository.getById(currentColorId).await()
            colorsRepository.setCurrentColor(currentColor).await()
            return@async currentColor
        }
                //вызовется в главном потоке как результат асинх операции
            .saveEnqueue(::onSaved)
//            .saveEnqueue { onSaved(it) }
    }
    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        load()
    }

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

    private fun load(){
        //запросим весь список доступных цветов
        // , и говорим into куда его нужно положить
        colorsRepository.getAvailableColors().into(_availableColors)
    }

    //сюда будет приходить сохраненный резалт
    private fun onSaved(result: FinalResult<NamedColor>){
        // уберем прогресс бар
        _saveInProgress.value = false
        // проверяем результат
        when(result){
            is SuccessResult -> navigator.goBack(result.data)
            is ErrorResult -> uiActions.toast(uiActions.getString(R.string.error_happened))
        }
    }
}
// val currentColor = colors.first { it.id == currentColorId }
// _screenTitle.value = uiActions.getString(R.string.change_color_screen_title, currentColor.name)

