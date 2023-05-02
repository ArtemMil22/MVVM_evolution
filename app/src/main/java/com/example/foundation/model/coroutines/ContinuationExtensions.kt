package com.example.foundation.model.coroutines

import com.example.foundation.model.ErrorResult
import com.example.foundation.model.FinalResult
import com.example.foundation.model.SuccessResult
import kotlinx.coroutines.CancellableContinuation
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Преобразование экземпляра сопрограммы [CancellableContinuation]
 * в более простой экземпляр [Emitter].
 */
fun <T> CancellableContinuation<T>.toEmitter(): Emitter<T> {

    return object : Emitter<T> {

        var done = AtomicBoolean(false)

        override fun emit(finalResult: FinalResult<T>) {
            if (done.compareAndSet(false, true)) {
                when (finalResult) {
                    is SuccessResult -> resume(finalResult.data)
                    is ErrorResult -> resumeWithException(finalResult.exception)
                }
            }
        }

        override fun setCancelListener(cancelListener: CancelListener) {
            invokeOnCancellation { cancelListener() }
        }
    }

}