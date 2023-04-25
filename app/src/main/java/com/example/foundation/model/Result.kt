package com.example.foundation.model

typealias Mapper<Input, Output> = (Input) -> Output

// Базовый класс, представляющий результат некоторой асинхронной операции.
sealed class Result<T> {

    fun <R> map(mapper: Mapper<T, R>? = null): Result<R> = when(this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if (mapper == null) throw IllegalArgumentException("Mapper should not be NULL for success result")
            SuccessResult(mapper(this.data))
        }
    }
}

sealed class FinalResult<T>: Result<T>()

class PendingResult<T> : Result<T>()

class SuccessResult<T>(
    val data: T,
) : FinalResult<T>()

class ErrorResult<T>(
    val exception: Exception,
) : FinalResult<T>()

//Получить значение успеха [Результат],
// если это возможно; в противном случае вернуть NULL.
fun <T> Result<T>?.takeSuccess(): T? {
    return if (this is SuccessResult)
        this.data
    else
        null
}

