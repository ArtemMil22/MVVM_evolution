package com.example.myapplication

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.ThreadUtils
import com.example.foundation.model.task.factories.ThreadTasksFactory
import com.example.foundation.model.task.dispatchers.MainThreadDispatcher
import com.example.foundation.model.task.factories.ExecutorServiceTasksFactory
import com.example.foundation.model.task.factories.HandlerThreadTasksFactory
import com.example.myapplication.model.colors.InMemoryColorsRepository
import java.util.concurrent.Executors

/**
 * Here we store instances of model layer classes.
 */

class App : Application(),BaseApplication {

    private val singleThreadExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newSingleThreadExecutor())
    private val handlerThreadTasksFactory = HandlerThreadTasksFactory()
    private val cachedThreadPoolExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newCachedThreadPool())
    private val threadUtils = ThreadUtils.Default()
    private val dispatcher =MainThreadDispatcher()

    override val singletonScopeDependencies:List<Any> = listOf(
        cachedThreadPoolExecutorTasksFactory,
        dispatcher,
        InMemoryColorsRepository(handlerThreadTasksFactory, threadUtils)
    )
}