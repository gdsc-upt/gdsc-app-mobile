package com.example.gdsc_app_mobile.interfaces

interface ISelectedEvent {
    fun onSelectedEvent(title: String, description: String, imageUri: String)
    abstract fun createPost(title: String, description: String, imageUri: String)
}