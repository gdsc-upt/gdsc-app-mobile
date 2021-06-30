package com.example.gdsc_app_mobile.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.models.MemberModel
import java.util.ArrayList

class MembersAdapter(private val context: Activity, private val arrayList: ArrayList<MemberModel>): ArrayAdapter<MemberModel>(context,
    R.layout.card_member, arrayList) {

    var position: Int = 0       //Position needed for color

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.card_member, null)

        val name: TextView = view.findViewById(R.id.member_name)

        colorElement(view.findViewById(R.id.member_card))   //Coloring the current item depending on position

        name.text = "${position + 1}. ${arrayList[position].name}" //Setting the number and the name of the member

        return view
    }

    private fun colorElement(card: View) {
        when(position % 4) {
            0 -> card.setBackgroundColor(context.getColor(R.color.gdsc_yellow))
            1 -> card.setBackgroundColor(context.getColor(R.color.gdsc_red))
            2 -> card.setBackgroundColor(context.getColor(R.color.gdsc_green))
            3 -> card.setBackgroundColor(context.getColor(R.color.gdsc_blue))
        }
    }
}