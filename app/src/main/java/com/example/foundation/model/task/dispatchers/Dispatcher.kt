package com.example.foundation.model.task.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)
}