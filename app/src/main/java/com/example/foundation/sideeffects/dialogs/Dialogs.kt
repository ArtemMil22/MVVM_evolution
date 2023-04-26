package com.example.foundation.sideeffects.dialogs

import com.example.foundation.model.task.Task
import com.example.foundation.sideeffects.dialogs.plugin.DialogConfig

interface Dialogs {

    /**
     * Show alert dialog to the user and wait for the user choice.
     */
    fun show(dialogConfig: DialogConfig): Task<Boolean>

}