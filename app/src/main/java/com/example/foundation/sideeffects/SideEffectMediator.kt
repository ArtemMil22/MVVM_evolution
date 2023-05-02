package com.example.foundation.sideeffects

import com.example.foundation.model.dispatchers.Dispatcher
import com.example.foundation.model.dispatchers.MainThreadDispatcher
import com.example.foundation.utils.ResourceActions

open class SideEffectMediator<Implementation>(
    dispatcher: Dispatcher = MainThreadDispatcher()
) {

    protected val target = ResourceActions<Implementation>(dispatcher)

    /**
     * Назначьте/отмените назначение целевой реализации для этого провайдера.
     */
    fun setTarget(target: Implementation?) {
        this.target.resource = target
    }

    fun clear() {
        target.clear()
    }

}