package com.example.myapplication.views.currentcolor

import com.example.foundation.model.PendingResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.takeSuccess
import com.example.foundation.model.task.dispatchers.Dispatcher
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MutableLiveResult
import com.example.myapplication.R
import com.example.myapplication.model.colors.NamedColor
import com.example.myapplication.views.changecolor.ChangeColorFragment
import ua.cn.stu.simplemvvm.model.colors.ColorListener
import ua.cn.stu.simplemvvm.model.colors.ColorsRepository

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    dispatcher: Dispatcher
) : BaseViewModel(dispatcher) {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- example of listening results via model layer
    init {
        colorsRepository.addListener(colorListener)
        load()
    }


override fun onCleared() {
    super.onCleared()
    colorsRepository.removeListener(colorListener)
}

override fun onResult(result: Any) {
    super.onResult(result)
    if (result is NamedColor) {
        val message = uiActions.getString(R.string.changed_color, result.name)
        uiActions.toast(message)
    }
}

fun changeColor() {
    val currentColor = currentColor.value.takeSuccess() ?: return
    val screen = ChangeColorFragment.Screen(currentColor.id)
    navigator.launch(screen)
}

fun tryAgain() {
    load()
}

    //использую saveEnqueue вместо enqueue , еще будет все задачи удаляться
    private fun load(){
        colorsRepository.getCurrentColor().into(_currentColor)
        }
    }