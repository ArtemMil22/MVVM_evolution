package com.example.foundation.sideeffects.permissions.plugin

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.task.Task
import com.example.foundation.model.task.callback.CallbackTask
import com.example.foundation.model.task.callback.Emitter
import com.example.foundation.sideeffects.SideEffectMediator
import com.example.foundation.sideeffects.permissions.Permissions

class PermissionsSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<PermissionsSideEffectImpl>(), Permissions {

    val retainedState = RetainedState()

    override fun hasPermissions(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(permission: String): Task<PermissionStatus> = CallbackTask.create { emitter ->
        if (retainedState.emitter != null) {
            emitter.emit(ErrorResult(IllegalStateException("Only one permission request can be active")))
            return@create
        }
        retainedState.emitter = emitter
        target { implementation ->
            implementation.requestPermission(permission)
        }
    }

    class RetainedState(
        var emitter: Emitter<PermissionStatus>? = null
    )

}