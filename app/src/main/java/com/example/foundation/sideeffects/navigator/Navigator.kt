package com.example.foundation.sideeffects.navigator

import com.example.foundation.views.BaseScreen

interface Navigator {

    /**
     * Запустите новый экран в верхней части заднего стека.
     */
    fun launch(screen: BaseScreen)

    /**
     * Вернитесь к предыдущему экрану и, при желании отправьте результат.
     */
    fun goBack(result: Any? = null)

}