package com.example.foundation

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.foundation.navigator.IntermediaNavigator
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions

const val ARG_SCREEN = "ARG_SCREEN"

/**
 * Implementation of [Navigator] and [UiActions].
 * It is based on activity view-model because instances of [Navigator] and [UiActions]
 * should be available from fragments' view-models (usually they are passed to the view-model constructor).
 *
 * This view-model extends [AndroidViewModel] which means that it is not "usual" view-model and
 * it may contain android dependencies (context, bundles, etc.).
 */
class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediaNavigator
) : ViewModel(), Navigator by navigator, UiActions by uiActions {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }
}