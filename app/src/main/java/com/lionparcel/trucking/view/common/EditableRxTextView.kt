package com.lionparcel.trucking.view.common

import android.widget.TextView
import com.jakewharton.rxbinding2.InitialValueObservable

object EditableRxTextView {
    fun textChanges(view: TextView): InitialValueObservable<CharSequence> {
        return EditableTextViewTextObservable(view)
    }
}
