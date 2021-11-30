package com.lionparcel.trucking.view.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

internal class EditableTextViewTextObservable(private val view: TextView) :
    InitialValueObservable<CharSequence>() {

    override fun subscribeListener(observer: Observer<in CharSequence>) {
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.addTextChangedListener(listener)
    }

    override fun getInitialValue(): CharSequence {
        return view.text
    }

    internal class Listener(
        private val view: TextView,
        private val observer: Observer<in CharSequence>
    ) : MainThreadDisposable(), TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (!isDisposed) {
                view.removeTextChangedListener(this)
                observer.onNext(s)
                view.addTextChangedListener(this)
            }
        }

        override fun onDispose() {
            view.removeTextChangedListener(this)
        }
    }
}
