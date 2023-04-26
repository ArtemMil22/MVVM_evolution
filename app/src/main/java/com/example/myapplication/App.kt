package com.example.myapplication

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.ThreadUtils
import com.example.foundation.model.coroutines.IoDispatcher
import com.example.foundation.model.dispatchers.MainThreadDispatcher

import com.example.myapplication.model.colors.InMemoryColorsRepository
import java.util.concurrent.Executors

/**
 * Here we store instances of model layer classes.
 */

class App : Application(),BaseApplication {

    private val ioDispatcher = IoDispatcher()

    override val singletonScopeDependencies:List<Any> = listOf(
        InMemoryColorsRepository(ioDispatcher)
    )
}