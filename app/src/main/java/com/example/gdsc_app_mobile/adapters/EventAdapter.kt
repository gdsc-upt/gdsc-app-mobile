package com.example.gdsc_app_mobile.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.models.EventModel

class EventAdapter(private val context : Activity, private val arrayList: ArrayList<EventModel>) :
ArrayAdapter<EventModel>(context, R.layout.card_event, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.card_event, null)

        val title : String = arrayList[position].title
        val description : String = arrayList[position].description

        return view
    }

}