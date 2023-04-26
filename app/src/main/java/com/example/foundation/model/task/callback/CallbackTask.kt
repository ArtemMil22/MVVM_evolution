package com.example.foundation.model.task.callback

import com.example.foundation.model.FinalResult
import com.example.foundation.model.task.AbstractTask
import com.example.foundation.model.task.SynchronizedTask
import com.example.foundation.model.task.Task
import com.example.foundation.model.task.TaskListener

class CallbackTask<T> private constructor(
    private val executionListener: ExecutionListener<T>
): AbstractTask<T>() {

    private var emitter: EmitterImpl<T>? = null

    override fun doEnqueue(listener: TaskListener<T>) {
        emitter = EmitterImpl(listener).also { executionListener(it) }
    }

    override fun doCancel() {
        emitter?.onCancelListener?.invoke()
    }

    companion object {
        fun <T> create(executionListener: ExecutionListener<T>): Task<T> {
            return SynchronizedTask(CallbackTask(executionListener))
        }
    }

    private class EmitterImpl<T>(
        private val taskListener: TaskListener<T>
    ) : Emitter<T> {
        var onCancelListener: CancelListener? = null

        override fun emit(finalResult: FinalResult<T>) {
            taskListener.invoke(finalResult)
        }

        override fun setCancelListener(cancelListener: CancelListener) {
            this.onCancelListener = cancelListener
        }
    }

}