package com.example.gdsc_app_mobile.adapters

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.interfaces.ISelectedDataTeams
import com.example.gdsc_app_mobile.models.TeamsModel
import java.util.*

class TeamsAdapter(private val context: Activity, private val arrayList: ArrayList<TeamsModel>): ArrayAdapter<TeamsModel>(context,
    R.layout.card_teams, arrayList){

    lateinit var listener: ISelectedDataTeams          //This is used for communication between this adapter and Teams fragment

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.card_teams, null)

        //Here specific fields are took from xml file using their IDs
        val title: TextView = view.findViewById(R.id.teams_title)
        val seeMembersButton: TextView = view.findViewById(R.id.see_members_button)

        //Set color for specific item from listview
        colorElement(view.findViewById(R.id.teams_card), title, position)

        //Set the title for the list item
        title.text = arrayList[position].name

        //See members button clicked
        seeMembers(seeMembersButton, position)

        return view
    }

    //Method used to change the color of an item depending on the position
    private fun colorElement(card: View,textView: TextView , position: Int) {
        when(position % 4) {
            0 -> setColor(card, R.color.gdsc_yellow, 20f)
            1 -> setColor(card, R.color.gdsc_red, 20f)
            2 -> setColor(card, R.color.gdsc_green, 20f)
            3 -> setColor(card, R.color.gdsc_blue, 20f)
        }
    }


    //Method used to set the color and corner radius at the same time
    private fun setColor(card: View, cardColor: Int, radius: Float) {
        val gradient = GradientDrawable()
        gradient.color = context.getColorStateList(cardColor)
        gradient.cornerRadius = radius
        card.background = gradient
    }

    //Method used for "See members" functionality
    private fun seeMembers(button: TextView, position: Int) {
        button.setOnClickListener {
            listener.seeMembers(arrayList[position], position)
        }
    }

    //Method used to set the listener (it is used when adapter is created in Teams fragment)
    fun addListener(listener: ISelectedDataTeams) {
        this.listener = listener
    }
}