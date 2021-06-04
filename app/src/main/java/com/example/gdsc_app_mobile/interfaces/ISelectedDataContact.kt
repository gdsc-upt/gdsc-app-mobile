package com.example.gdsc_app_mobile.interfaces

interface ISelectedDataContact {
    fun onSelectedData(name: String, email: String, subject: String, message: String)
}