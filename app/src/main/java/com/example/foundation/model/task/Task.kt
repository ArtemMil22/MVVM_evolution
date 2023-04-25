package com.example.foundation.model.task

import com.example.foundation.model.FinalResult

typealias TaskListener<T> = (FinalResult<T>) -> Unit

interface Task<T> {

    fun await():T

    //запускать будем в главном потоке
    fun enqueue(listener: TaskListener<T>)

    fun cancel()
}