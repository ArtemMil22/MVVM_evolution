package com.example.myapplication

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.Repository
import com.example.myapplication.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */

class App : Application(),BaseApplication {

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val repositories = listOf<Repository>(
        InMemoryColorsRepository()
    )

}