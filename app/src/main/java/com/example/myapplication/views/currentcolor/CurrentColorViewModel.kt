package com.example.myapplication.views.currentcolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.myapplication.R
import com.example.myapplication.model.colors.NamedColor
import com.example.myapplication.views.Navigator
import com.example.myapplication.views.UiActions
import com.example.myapplication.views.base.BaseViewModel
import com.example.myapplication.views.changecolor.ChangeColorFragment
import ua.cn.stu.simplemvvm.model.colors.ColorListener
import ua.cn.stu.simplemvvm.model.colors.ColorsRepository

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveData<NamedColor>()
    val currentColor: LiveData<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    // --- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}