package com.example.foundation.views

import androidx.lifecycle.*
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.Result
import com.example.foundation.model.SuccessResult
import com.example.foundation.utils.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

open class BaseViewModel : ViewModel() {

    private val coroutineContext =
        SupervisorJob()+ Dispatchers.Main.immediate + CoroutineExceptionHandler { _, throwable ->
        // вы можете добавить сюда обработку исключений
    }

    // настраиваемая область, которая сразу же отменяет задания при нажатии кнопки «Назад»
    protected val viewModelScope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        clearScope()
    }

    /**
     * Переопределите этот метод в дочерних классах, если хотите прослушивать результаты.
     * с других экранов
     */
    open fun onResult(result: Any) {
    }

    open fun onBackPressed(): Boolean {
        clearScope()
        return false
    }

    /**
     * Запустите указанный приостанавливающий [блок] и используйте его результат в качестве значения для
     * при условии [liveResult].
     */
    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            } catch (e: Exception) {
                if (e !is CancellationException) liveResult.postValue(ErrorResult(e))
            }
        }
    }

    fun <T> into(stateFlow: MutableStateFlow<Result<T>>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                stateFlow.value = SuccessResult(block())
            } catch (e: Exception) {
                if (e !is CancellationException) stateFlow.value = ErrorResult(e)
            }
        }
    }

    // преобразование Flow к savedStateHandle
    fun <T> SavedStateHandle.getMyStateFlow(key: String, initialValue: T): MutableStateFlow<T> {
        val savedStateHandle = this
        val mutableFlow = MutableStateFlow(savedStateHandle[key] ?: initialValue)

        viewModelScope.launch {
            mutableFlow.collect {
                savedStateHandle[key] = it
            }
        }

        viewModelScope.launch {
            savedStateHandle.getLiveData<T>(key).asFlow().collect {
                mutableFlow.value = it
            }
        }
        return mutableFlow
    }

    private fun clearScope() {
        viewModelScope.cancel()
    }
}