package  com.example.simplemvvm.model.colors

import com.example.foundation.model.Repository
import com.example.myapplication.model.colors.NamedColor
import kotlinx.coroutines.flow.Flow

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    /**
     * Получить список всех доступных цветов, которые может выбрать пользователь.
     */
    suspend fun getAvailableColors(): List<NamedColor>

    /**
     * Получить цветовое содержимое по его идентификатору
     */
    suspend fun getById(id: Long): NamedColor

    /**
     * Получить текущий выбранный цвет.
     */
    suspend fun getCurrentColor(): NamedColor

    /**
     * Установите указанный цвет в качестве текущего.
     */
    fun setCurrentColor(color: NamedColor):Flow<Int>

   fun listenCurrentColor():Flow<NamedColor>
}