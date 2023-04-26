package ua.cn.stu.simplemvvm.model.colors

import com.example.foundation.model.Repository
import com.example.foundation.model.task.Task
import com.example.myapplication.model.colors.NamedColor

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    fun getAvailableColors(): Task<List<NamedColor>>

    fun getById(id: Long): Task<NamedColor>

    fun getCurrentColor(): Task<NamedColor>

    fun setCurrentColor(color: NamedColor): Task<Unit>

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)

}