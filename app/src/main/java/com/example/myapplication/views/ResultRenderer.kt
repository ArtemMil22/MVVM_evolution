package com.example.myapplication.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.foundation.model.Result
import com.example.foundation.views.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.PartResultBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * Рендеринг по умолчанию [Результат].
 * - если [результат] равен [PendingResult] -> отображается только прогресс-бар
 * - если [результат] равен [ErrorResult] -> отображается только контейнер ошибок
 * - если [результат] равен [SuccessResult] -> контейнер ошибок и индикатор выполнения скрыты, все остальные представления видны
 */
fun <T> BaseFragment.renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
    val binding = PartResultBinding.bind(root)

    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        }
    )
}

/**
 * прослушиватель onClick для кнопки повторной попытки по умолчанию.
 */
fun BaseFragment.onTryAgain(root: View, onTryAgainPressed: () -> Unit) {
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener { onTryAgainPressed() }
}

/**
 * Collect items from the specified [Flow] only when fragment is at least in STARTED state.
 */
fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        // this coroutine is cancelled in onDestroyView
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            // this coroutine is launched every time when onStart is called;
            // collecting is cancelled in onStop
            flow.collect {
                onCollect(it)
            }
        }
    }
}