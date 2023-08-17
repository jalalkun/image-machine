package com.jalalkun.imagemachine.utils

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }
    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }
}