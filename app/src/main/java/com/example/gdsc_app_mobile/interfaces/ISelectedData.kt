package com.example.gdsc_app_mobile.interfaces

import com.example.gdsc_app_mobile.models.FaqModel

interface ISelectedData {
    fun onSelectedData(question: String, answer: String)
    fun deleteFaq(faq: FaqModel?)
}