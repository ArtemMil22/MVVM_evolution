package com.example.myapplication

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.Repository
import com.example.foundation.model.task.SimpleTaskFactory
import com.example.myapplication.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */

class App : Application(),BaseApplication {

    private val tasksFactory = SimpleTaskFactory()

    override val repositories = listOf<Repository>(
        tasksFactory,
        InMemoryColorsRepository(tasksFactory)
    )

}