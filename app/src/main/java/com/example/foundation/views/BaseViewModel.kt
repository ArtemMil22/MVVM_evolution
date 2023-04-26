package com.example.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foundation.model.PendingResult
import com.example.foundation.utils.Event
import com.example.foundation.model.Result
import com.example.foundation.model.task.Task
import com.example.foundation.model.task.TaskListener
import com.example.foundation.model.task.dispatchers.Dispatcher

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

open class BaseViewModel(
    private val dispatcher: Dispatcher
) : ViewModel() {

    //поле содержащее список задач
    private val tasks = mutableSetOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        //проходим по списку задач и отменяем
        tasks.forEach { it.cancel() }
        tasks.clear()

    }

    open fun onResult(result: Any) {
    }

    fun <T> Task<T>.saveEnqueue(listener: TaskListener<T>? = null) {
// задачу которую использовали добавим в список наших задач
        tasks.add(this)
        //задача завершилась и..
        this.enqueue(dispatcher) {
            tasks.remove(this)
            listener?.invoke(it)
        }
    }

    // для добавления  в LD, чтобы не дублировть одинаковый код часто
        fun <T> Task<T>.into(liveResult: MutableLiveResult<T>){
        liveResult.value =PendingResult()
        this.saveEnqueue {
            liveResult.value = it
        }
    }

}