package com.lionparcel.trucking.data.common.base

abstract class BaseMapper<T, R> {

    fun apply(item: T): R {
        return map(item)
    }

    fun apply(list: List<T>): List<R> {
        return list.map {
            apply(it)
        }
    }

    protected abstract fun map(item: T): R
}
