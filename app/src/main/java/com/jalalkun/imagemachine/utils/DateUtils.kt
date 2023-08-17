package com.jalalkun.imagemachine.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.util.Date
import java.util.Locale

fun convertMillisToStringDate(millis: Long, dateFormat: String): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(Date(millis))
}

fun String?.toDate():Date?{
    if (this == null) return null
    return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(this)
}