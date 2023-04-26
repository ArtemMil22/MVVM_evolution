package com.example.foundation.model.task.factories

import com.example.foundation.model.task.AbstractTask
import com.example.foundation.model.task.SynchronizedTask
import com.example.foundation.model.task.Task
import com.example.foundation.model.task.TaskListener


class ThreadTasksFactory : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ThreadTask(body))
    }

    private class ThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            thread = Thread {
                executeBody(body, listener)
            }
            thread?.start()
        }

        override fun doCancel() {
            thread?.interrupt()
        }

    }

}