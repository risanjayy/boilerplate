package com.lionparcel.services.consumer.domain.common.extensions

import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.add(calendarFieldToAdd: Int, unitToAdd: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(calendarFieldToAdd, unitToAdd)
    return Date(calendar.timeInMillis)
}

fun Date.subtract(dateToSubtractBy: Date, resultTimeUnit: TimeUnit): Long {
    return resultTimeUnit.convert(this.time - dateToSubtractBy.time, TimeUnit.MILLISECONDS)
}
