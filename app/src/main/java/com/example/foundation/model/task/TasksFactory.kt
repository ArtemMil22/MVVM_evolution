package com.example.foundation.model.task

import com.example.foundation.model.Repository

typealias TaskBody<T> = () -> T

//создание асинхронных задач
interface TasksFactory : Repository {

    fun <T> async(body: TaskBody<T>): Task<T>
}