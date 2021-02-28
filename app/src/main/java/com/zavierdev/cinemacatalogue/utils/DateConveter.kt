package com.zavierdev.cinemacatalogue.utils

import androidx.room.TypeConverter
import java.sql.Date

class DateConveter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}