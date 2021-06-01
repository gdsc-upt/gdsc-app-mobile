package com.example.gdsc_app_mobile.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.models.FaqModel
import java.util.ArrayList

class FaqAdapter(private val context: Activity, private val arrayList: ArrayList<FaqModel>): ArrayAdapter<FaqModel>(context,
    R.layout.card_faq, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.card_faq, null)

        val question: TextView = view.findViewById(R.id.faq_question)
        val answer: TextView = view.findViewById(R.id.faq_answer)
        val dates: TextView = view.findViewById(R.id.faq_date)

        question.text = arrayList[position].question
        answer.text = arrayList[position].answer
        dates.text = getCurrentDate(arrayList[position].created)

        return view
    }

    private fun getCurrentDate(dateString: String): String {

        val year = dateString.substring(0, 4)
        val month = dateString.substring(5, 7)
        val day = dateString.substring(8,10)

        return "$day/$month/$year"

    }
}