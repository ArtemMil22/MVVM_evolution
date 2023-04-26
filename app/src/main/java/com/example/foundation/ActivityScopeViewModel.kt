package com.example.foundation

import androidx.lifecycle.ViewModel
import com.example.foundation.sideeffects.SideEffectMediator
import com.example.foundation.sideeffects.SideEffectMediatorsHolder

//const val ARG_SCREEN = "ARG_SCREEN"

class ActivityScopeViewModel : ViewModel() {

    internal val sideEffectMediatorsHolder = SideEffectMediatorsHolder()

    val sideEffectMediators: List<SideEffectMediator<*>>
        get() = sideEffectMediatorsHolder.mediators

    override fun onCleared() {
        super.onCleared()
        sideEffectMediatorsHolder.clear()
    }

}