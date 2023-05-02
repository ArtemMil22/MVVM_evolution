package com.example.myapplication

import com.example.foundation.SingletonScopeDependencies
import com.example.foundation.model.coroutines.IoDispatcher
import com.example.foundation.model.coroutines.WorkerDispatcher
import com.example.myapplication.model.colors.InMemoryColorsRepository

object Initializer {

    // Поместите здесь свои одноэлементные зависимости области видимости
    fun initDependencies() = SingletonScopeDependencies.init { applicationContext ->
        // этот блок кода выполняется только один раз при первом запросе

        // классы держателей используются, потому что у нас есть 2 диспетчера одного типа
        val ioDispatcher = IoDispatcher() // для операций ввода-вывода
        val workerDispatcher =
            WorkerDispatcher() //  для операций с интенсивным использованием процессора

        return@init listOf(
            ioDispatcher,
            workerDispatcher,

            InMemoryColorsRepository(ioDispatcher)
        )
    }
}