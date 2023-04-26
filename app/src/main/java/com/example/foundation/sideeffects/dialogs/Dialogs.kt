package com.example.foundation.sideeffects.dialogs

import com.example.foundation.sideeffects.dialogs.plugin.DialogConfig

interface Dialogs {

    /**
     * Show alert dialog to the user and wait for the user choice.
     */
   suspend fun show(dialogConfig: DialogConfig): Boolean

}