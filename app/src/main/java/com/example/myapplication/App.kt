package com.example.myapplication

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.coroutines.IoDispatcher
import com.example.myapplication.model.colors.InMemoryColorsRepository

/**
 * Здесь мы храним экземпляры классов слоя модели.
 */

class App : Application(), BaseApplication {

    private val ioDispatcher = IoDispatcher()

    override val singletonScopeDependencies: List<Any> = listOf(
        InMemoryColorsRepository(ioDispatcher)
    )
}