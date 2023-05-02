package com.example.foundation.sideeffects

import android.content.Context

interface SideEffectPlugin<Mediator, Implementation> {

    /**
     * Класс медиатора side-effect.
     */
    val mediatorClass: Class<Mediator>

    /**
     * Для создания экземпляра посредника, который действует
     * на стороне модели представления.
     */
    fun createMediator(applicationContext: Context): SideEffectMediator<Implementation>

    /**
     * Для создания реализации медиатора, созданного методом [createMediator].
     * Может возвращать `null`. NULL-значение может быть полезно, если логика может быть реализована непосредственно в посреднике.
     * (например, побочный эффект не требует экземпляра активности)
     */
    fun createImplementation(mediator: Mediator): Implementation? = null

}