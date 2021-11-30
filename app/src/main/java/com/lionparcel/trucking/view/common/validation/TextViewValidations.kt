package com.lionparcel.trucking.view.common.validation

import android.widget.TextView

object TextViewValidations {

    private const val PHONE_NUMBER_REGEX = "08+[1235789]+([0-9]+)"
    private const val PHONE_NUMBER_MIN_LENGTH = 10
    private const val PHONE_NUMBER_MAX_LENGTH = 13

    fun validateIsNotEmpty() = { textView: TextView ->
        textView.text.isNotBlank()
    }

    fun validateLengthInRange(range: IntRange) = { textView: TextView ->
        textView.text.length in range
    }

    fun validatePasswordConfirmation(compareTo: String) = { textView: TextView ->
        textView.text.toString() == compareTo
    }

    fun validateMatchRegex(regex: Regex) = { textView: TextView ->
        textView.text.matches(regex)
    }

    fun validatePhoneNumberFormat() = { textView: TextView ->
        textView.text.matches(Regex(PHONE_NUMBER_REGEX))
    }

    fun validatePhoneNumberLength() = { textView: TextView ->
        textView.text.length in PHONE_NUMBER_MIN_LENGTH..PHONE_NUMBER_MAX_LENGTH
    }
}
