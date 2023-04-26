package com.example.foundation.sideeffects

import android.content.Context

@Suppress("UNCHECKED_CAST")
class SideEffectMediatorsHolder {

    private val _mediators = mutableMapOf<Class<*>, SideEffectMediator<*>>()
    val mediators: List<SideEffectMediator<*>>
        get() = _mediators.values.toList()

    /**
     * Существует ли [SideEffectMediator] указанного класса или нет.
     */
    fun <T> contains(clazz: Class<T>): Boolean {
        return _mediators.contains(clazz)
    }

    /**
     * Создайем и сохраняем [SideEffectMediator], используя указанный [SideEffectPlugin].
     */
    fun <Mediator, Implementation> putWithPlugin(
        applicationContext: Context,
        plugin: SideEffectPlugin<Mediator, Implementation>
    ) {
        _mediators[plugin.mediatorClass] = plugin.createMediator(applicationContext)
    }

    /**
     * Связываем [SideEffectImplementation] с [SideEffectMediator].
     * Таким образом, посредник может доставлять все звонки к реализации.
     */
    fun <Mediator, Implementation> setTargetWithPlugin(
        plugin: SideEffectPlugin<Mediator, Implementation>,
        sideEffectImplementationsHolder: SideEffectImplementationsHolder,
    ) {
        val intermediateViewService = get(plugin.mediatorClass)
        val target = sideEffectImplementationsHolder.getWithPlugin(plugin)
        if (intermediateViewService is SideEffectMediator<*>) {
            (intermediateViewService as SideEffectMediator<Implementation>).setTarget(target)
        }
    }

    /**
     * Получаем экземпляр [SideEffectMediator] по его классу.
     */
    fun <T> get(clazz: Class<T>): T {
        return _mediators[clazz] as T
    }

    /**
     * Отвязать все экземпляры [SideEffectImplementation] от всех экземпляров [SideEffectMediator].
     */
    fun removeTargets() {
        _mediators.values.forEach { it.setTarget(null) }
    }

    /**
     * Убрать всех посредников.
     */
    fun clear() {
        _mediators.values.forEach { it.clear() }
        _mediators.clear()
    }

}