package com.lionparcel.trucking.view.common.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val LOCALE_INDONESIA = "in"
    private const val UTC_TIMEZONE = "UTC"

    fun dateToString(date: Date, format: String, isLocalTimeZone: Boolean = false): String {
        val dateFormat = buildDateFormat(format)
        if (isLocalTimeZone) dateFormat.timeZone = TimeZone.getDefault()
        return dateFormat.format(date)
    }

    fun stringToDate(dateString: String, format: String): Date {
        return buildDateFormat(format).parse(dateString)
    }

    private fun buildDateFormat(format: String) = when (format) {
        TIME_STAMP_FORMAT -> buildUtcDateFormat(format)
        else -> buildSimpleDateFormat(format)
    }

    fun buildSimpleDateFormat(format: String) =
        SimpleDateFormat(format, Locale(LOCALE_INDONESIA))

    private fun buildUtcDateFormat(format: String) =
        SimpleDateFormat(format, Locale(LOCALE_INDONESIA)).apply {
            timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
        }
}
