package com.example.foundation.model.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Holder для диспетчеров
 */
class IoDispatcher(
    val value: CoroutineDispatcher = Dispatchers.IO
)

class WorkerDispatcher(
    val value: CoroutineDispatcher = Dispatchers.Default
)