package com.example.foundation.model.coroutines

import com.example.foundation.model.FinalResult

typealias CancelListener = () -> Unit

interface Emitter<T> {

    /**
     * Finish the associated task with the specified result.
     */
    fun emit(finalResult: FinalResult<T>)

    /**
     * Assign optional cancel listener. This listener is executed when the associated task
     * has been cancelled by [Task.cancel] call.
     */
    fun setCancelListener(cancelListener: CancelListener)

    companion object {
        /**
         * Wrap the emitter with some [onFinish] action which will be executed upon
         * publishing result or cancelling. May be useful for cleanup logic.
         */
        fun <T> wrap(emitter: Emitter<T>, onFinish: () -> Unit): Emitter<T> {
            return object : Emitter<T> {
                override fun emit(finalResult: FinalResult<T>) {
                    onFinish()
                    emitter.emit(finalResult)
                }

                override fun setCancelListener(cancelListener: CancelListener) {
                    emitter.setCancelListener {
                        onFinish()
                        cancelListener()
                    }
                }
            }
        }
    }
}