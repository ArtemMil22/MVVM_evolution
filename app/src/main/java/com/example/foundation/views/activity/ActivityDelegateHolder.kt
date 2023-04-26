package com.example.foundation.views.activity

/**
 * Если вы по какой-то причине не хотите использовать [BaseActivity] (например, у вас есть 2 или более активности
 * иерархии, то вместо этого можно использовать этот держатель.
 * Обратите внимание, что в этом случае вам нужно вызывать методы [delegate] вручную из вашей активности.
 * Подробнее см. [ActivityDelegate].
 */
interface ActivityDelegateHolder {

    val delegate: ActivityDelegate

}