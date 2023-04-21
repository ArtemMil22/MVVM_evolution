package com.example.foundation.views

import com.example.foundation.ActivityScopeViewModel

interface FragmentsHolder {

   fun notifyScreenUpdates()

   fun getActivityScopeViewModel(): ActivityScopeViewModel
}