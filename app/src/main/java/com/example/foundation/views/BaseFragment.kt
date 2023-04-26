package com.example.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.PendingResult
import  com.example.foundation.model.Result
import com.example.foundation.model.SuccessResult
import com.example.foundation.views.activity.ActivityDelegateHolder

//**
// Базовый класс для всех фрагментов
abstract class BaseFragment : Fragment() {

    /**
     * View-model которая управляет этим фрагментом
     */
    abstract val viewModel: BaseViewModel

    /**
     * Вызовите этот метод, когда элементы управления действиями
     * (например, панель инструментов) должны быть повторно отображены.
     */
    fun notifyScreenUpdates() {
        (requireActivity() as ActivityDelegateHolder).delegate.notifyScreenUpdates()
    }

    /**
     * Скрыть все представления в [root], а затем вызвать одну из предоставленных лямбда-функций
     * в зависимости от [результата]:
     * - [onPending] вызывается, когда [result] равен [PendingResult]
     * - [onSuccess] вызывается, когда [result] равен [SuccessResult]
     * - [onError] вызывается, когда [result] равен [ErrorResult]
     */
    fun <T> renderResult(root: ViewGroup, result: Result<T>,
                         onPending: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {

        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }

    }
}