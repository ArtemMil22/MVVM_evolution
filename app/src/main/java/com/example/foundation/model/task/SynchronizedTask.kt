package com.example.foundation.model.task

import com.example.foundation.model.task.dispatchers.Dispatcher
import java.util.concurrent.atomic.AtomicBoolean

class SynchronizedTask<T>(
    private val task: Task<T>,
) : Task<T> {

    @Volatile
    private var cancelled = false

    private var executed = false

    private var listenerCalled = AtomicBoolean(false)

    // синхрон запуск
    override fun await(): T {
        synchronized(this) {
            if (cancelled) throw CancelledException()
            if (executed) throw IllegalStateException("Task has been executed")
            executed = true
        }
        // await is out of synchronized block to allow cancelling from another thread
        return task.await()
    }

    // асинхрон запуск
    override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) =
        synchronized(this) {
            if (cancelled) return
            if (executed) throw IllegalStateException("Task has been executed")
            executed = true

            val finalListener: TaskListener<T> = { result ->
                // this code block is not synchronized because it is launched after enqueue() method finishes
                if (listenerCalled.compareAndSet(false, true)) {
                    if (!cancelled) listener(result)
                }
            }
            task.enqueue(dispatcher, finalListener)
        }

    // отмена запуск
    override fun cancel() = synchronized(this) {
        if (listenerCalled.compareAndSet(false, true)) {
            if (cancelled) return
            cancelled = true
            task.cancel()
        }
    }
}