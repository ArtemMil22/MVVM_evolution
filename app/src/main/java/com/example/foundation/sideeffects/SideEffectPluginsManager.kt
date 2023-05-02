package com.example.foundation.sideeffects

class SideEffectPluginsManager {

    private val _plugins = mutableListOf<SideEffectPlugin<*, *>>()
    internal val plugins: List<SideEffectPlugin<*, *>>
        get() = _plugins

    /**
     * Зарегистрируем новый плагин side-effect.
     * Интерфейс посредника, заданный плагином, можно использовать
     * в конструкторе моделей представления.
     */
    fun <Mediator, Implementation> register(plugin: SideEffectPlugin<Mediator, Implementation>) {
        _plugins.add(plugin)
    }

}