package com.example.foundation.model.coroutines

import com.example.foundation.model.FinalResult

typealias CancelListener = () -> Unit

interface Emitter<T> {

    /**
     * Завершаю связанную задачу с указанным результатом.
     */
    fun emit(finalResult: FinalResult<T>)

    /**
     * Назначаем дополнительный прослушиватель отмены.
     * Этот прослушиватель выполняется, когда связанная задача
     * была отменено вызовом
     */
    fun setCancelListener(cancelListener: CancelListener)

    companion object {
        /**
         * Оберните эмиттер некоторым действием [onFinish],
         * которое будет выполняться при публикации результата или отмена.
         * Может быть полезно для логики очистки.
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