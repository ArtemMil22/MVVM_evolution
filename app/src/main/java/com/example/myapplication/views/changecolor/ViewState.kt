package com.example.myapplication.views.changecolor

// делаю ради кноки с прогресс баром
data class ViewState(
    val colorsList: List<NamedColorListItem>,
    val showSaveButton: Boolean,
    val showCancelButton: Boolean,
    val showSaveProgressBar: Boolean,
    // полосочка
    val saveProgressPercentage: Int,
    // отображение сообщения
    val saveProgressPercentageMessage:String
)