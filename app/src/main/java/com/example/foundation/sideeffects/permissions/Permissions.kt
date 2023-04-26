package com.example.foundation.sideeffects.permissions

import com.example.foundation.model.task.Task
import com.example.foundation.sideeffects.permissions.plugin.PermissionStatus

interface Permissions {

    /**
     * Whether the app has the specified permission or not.
     */
    fun hasPermissions(permission: String): Boolean

    /**
     * Request the specified permission.
     * See [PermissionStatus]
     */
    fun requestPermission(permission: String): Task<PermissionStatus>

}