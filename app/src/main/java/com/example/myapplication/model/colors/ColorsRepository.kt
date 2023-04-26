package  com.example.simplemvvm.model.colors

import com.example.foundation.model.Repository
import com.example.myapplication.model.colors.NamedColor
import kotlinx.coroutines.flow.Flow

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    /**
     * Get the list of all available colors that may be chosen by the user.
     */
    suspend fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color content by its ID
     */
    suspend fun getById(id: Long): NamedColor

    /**
     * Get the current selected color.
     */
    suspend fun getCurrentColor(): NamedColor

    /**
     * Set the specified color as current.
     */
    fun setCurrentColor(color: NamedColor):Flow<Int>

   fun listenCurrentColor():Flow<NamedColor>
}