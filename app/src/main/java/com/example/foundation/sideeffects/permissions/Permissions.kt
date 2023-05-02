package com.example.foundation.sideeffects.permissions

import com.example.foundation.sideeffects.permissions.plugin.PermissionStatus

interface Permissions {

    /**
     * Для проверки - Имеет ли приложение указанное разрешение или нет.
     */
    fun hasPermissions(permission: String): Boolean

    /**
     * Запросить указанное разрешение.
     * См. [Статус разрешения]
     */
   suspend fun requestPermission(permission: String): PermissionStatus

}