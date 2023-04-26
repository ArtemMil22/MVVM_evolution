package com.example.foundation.model.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)
}