package com.example.foundation.model.task.factories

import com.example.foundation.model.task.Task

typealias TaskBody<T> = () -> T

//создание асинхронных задач
interface TasksFactory{

    // используем для запоска синхронного кода асинхронно
    fun <T> async(body: TaskBody<T>): Task<T>
}