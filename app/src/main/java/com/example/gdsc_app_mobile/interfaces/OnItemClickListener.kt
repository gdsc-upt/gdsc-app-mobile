package com.example.gdsc_app_mobile.interfaces

import com.example.gdsc_app_mobile.models.FaqModel

interface OnItemClickListener {
    fun onItemClick(faq: FaqModel?)
    fun onLongItemClick(faq: FaqModel?)
}