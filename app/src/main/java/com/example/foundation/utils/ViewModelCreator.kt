package com.example.foundation.utils

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


typealias ViewModelCreator = () -> ViewModel?

@Suppress("UNCHECKED_CAST")
class ViewModelFactoryFound(
    private val creatorR: ViewModelCreator,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creatorR() as T
    }
}

inline fun <reified VM : ViewModel> ComponentActivity.viewModelCreator(noinline creator: ViewModelCreator): Lazy<VM> {
    return viewModels { ViewModelFactoryFound(creator) }
}