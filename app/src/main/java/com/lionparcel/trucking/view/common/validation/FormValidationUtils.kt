package com.lionparcel.trucking.view.common.validation

import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

object FormValidationUtils {

    fun validate(
        validations: List<Validation<*>>,
        eventValidationCallback: ((Validation<*>) -> Unit)? = null
    ): Observable<Boolean> {
        var observable = Observable.empty<Boolean>()
        validations.forEach {
            observable = observable.mergeWith(validate(it, eventValidationCallback))
        }
        return observable.map {
            validations.asSequence().map { it.isViewValid() }.reduce { acc, valid -> acc && valid }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun validate(
        validation: Validation<*>,
        eventValidationCallback: ((Validation<*>) -> Unit)?
    ): Observable<Boolean> {
        return when (validation.view) {
            is TextView -> validateTextView(
                validation as Validation<TextView>,
                eventValidationCallback
            )
            else -> throw Throwable("Unable to validate ${validation.view::class.java}")
        }
    }

    private fun validateTextView(
        validation: Validation<TextView>,
        eventValidationCallback: ((Validation<TextView>) -> Unit)?
    ): Observable<Boolean> {
        return RxTextView.textChanges(validation.view)
            .skipInitialValue()
            .doOnNext { eventValidationCallback?.invoke(validation) }
            .map { validation.isViewValid() }
    }

    fun validateDirectly(
        validations: List<Validation<*>>,
        eventValidationCallback: ((Validation<*>) -> Unit),
        validationValidCallback: () -> Unit,
        validationErrorCallback: (() -> Unit)? = null
    ) {
        val errorValidations = validations.filterNot(Validation<*>::isViewValid)
        if (errorValidations.isEmpty()) {
            validationValidCallback()
        } else {
            validationErrorCallback?.invoke()
        }
        validations.groupBy { it.view }.map { entry ->
            entry.value.filterNot(Validation<*>::isViewValid).maxByOrNull { it.priority } ?: entry.value.first()
        }.map(eventValidationCallback)
    }
}
