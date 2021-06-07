package com.example.gdsc_app_mobile.adapters

import android.view.View
import android.widget.TextView
import com.example.gdsc_app_mobile.R
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view)  {
    val textView: TextView = view.findViewById(R.id.calendarDayText)
    
}