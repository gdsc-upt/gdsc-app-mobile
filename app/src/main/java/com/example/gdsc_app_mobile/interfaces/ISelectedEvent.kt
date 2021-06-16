package com.example.gdsc_app_mobile.interfaces

import com.example.gdsc_app_mobile.models.EventModel

interface ISelectedEvent {//to be worked on
    fun onSelectedEvent(title: String, description: String)
    abstract fun createPost(title: String, description: String)
    abstract fun daysOfWeekFromLocale(): Any
    abstract fun deleteEvent(it: EventModel)
}