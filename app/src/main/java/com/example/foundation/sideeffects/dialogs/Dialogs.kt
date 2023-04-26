package com.example.foundation.sideeffects.dialogs

import com.example.foundation.sideeffects.dialogs.plugin.DialogConfig

interface Dialogs {

    /**
     * Для Показа диалогового окна предупреждения пользователю
     * и дождаться выбора пользователя.
     */
   suspend fun show(dialogConfig: DialogConfig): Boolean

}