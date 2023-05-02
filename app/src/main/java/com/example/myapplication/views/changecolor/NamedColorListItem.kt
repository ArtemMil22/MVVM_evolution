package com.example.myapplication.views.changecolor

import com.example.myapplication.model.colors.NamedColor

// c доп значением выбран цвет или нет

data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)