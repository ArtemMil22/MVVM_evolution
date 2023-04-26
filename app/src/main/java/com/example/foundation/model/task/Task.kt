package com.example.foundation.model.task

import com.example.foundation.model.FinalResult
import com.example.foundation.model.task.dispatchers.Dispatcher

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    originException: Exception? = null
) : Exception(originException)

interface Task<T> {

    fun await():T

    //запускать будем в главном потоке
    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    fun cancel()
}