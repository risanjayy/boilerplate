package com.lionparcel.trucking.view.common.validation

import android.view.View

class Validation<T : View>(
    val view: T,
    val isValid: (T) -> Boolean,
    val data: Map<String, Any>? = null,
    val priority: Int = 0
) {

    fun isViewValid(): Boolean = isValid(view)
}
