package com.example.foundation.navigator

import com.example.foundation.utils.ResourceActions
import com.example.foundation.views.BaseScreen

//тут будет реализована очередь навигатора
class IntermediaNavigator : Navigator {

    private val targetNavigator = ResourceActions<Navigator>()

    override fun launch(screen: BaseScreen) = targetNavigator {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = targetNavigator {
        it.goBack(result)
    }

    // методы чтобы назначать навигатор,
// который реализует саму навигацию в targetNavigator

    fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    fun clear() {
        targetNavigator.clear()
    }
}