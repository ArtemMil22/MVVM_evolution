package com.example.foundation.model

sealed class Progress

object EmptyProgress : Progress()

data class PercentageProgress(
    val percentage: Int
): Progress() {
    // делаем для того чтобы каждый раз не повторялось получение процента
    companion object{
        val START = PercentageProgress(percentage = 0)
    }

}

// --- extension methods true когда Empty

fun Progress.isInProgress() = this !is EmptyProgress

// вытягивать процент c обработкой

fun Progress.getPercentage() = (this as? PercentageProgress)
    ?.percentage
    ?: PercentageProgress.START.percentage