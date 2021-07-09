package com.example.gdsc_app_mobile.models

class FaqModel(var question: String, var answer: String, var id: String?, var created: String?, var updated: String?) {
    var isExpanded: Boolean = false
}