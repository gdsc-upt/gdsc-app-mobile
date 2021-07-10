package com.example.gdsc_app_mobile.models

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class EventModel(
    var title: String, var description: String,
    var image: String, var start: String, var end: String) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd", Locale.ENGLISH)

    override fun toString(): String {
        return "EventModel(title='$title', description='$description', image='$image', start='$start', end='$end')"
    }

    fun getDate(): LocalDate {
        val t1 = 0
        val t2 = 9
        val string = start.subSequence(t1, t2)
        return LocalDate.parse(string, formatter)
    }

}

