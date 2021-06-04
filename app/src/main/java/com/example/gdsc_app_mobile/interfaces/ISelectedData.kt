package com.example.gdsc_app_mobile.interfaces

interface ISelectedData {
    fun onSelectedData(question: String, answer: String)
    fun deletePosition(position: Int)
}