package com.example.gdsc_app_mobile.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.interfaces.ISelectedDataTeams
import com.example.gdsc_app_mobile.interfaces.OnItemClickListener
import com.example.gdsc_app_mobile.models.TeamsModel

class RVAdapterTeams(var context: Context, var listener: OnItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var arrayList = emptyList<TeamsModel>()
    lateinit var listenerFragment: ISelectedDataTeams          //This is used for communication between this adapter and Teams fragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.card_view_teams, parent, false)
        return TeamsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val type : TeamsModel = arrayList[position]          //Getting the specific team from array
        (holder as TeamsViewHolder)
            .initializeUIComponents(type.name)

        HelperClass.adminRole(holder.deleteTeam)            //Delete team functionality only for admin

        colorElement(holder.itemView, position)             //Coloring the team cards depending on it's position

        deleteTeam(holder.deleteTeam, position)             //Delete team functionality

        seeMembers(holder.seeMembers, position)             //See members functionality
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun setTeam(list: List<TeamsModel>){
        this.arrayList = list
        notifyDataSetChanged()
    }

    inner class TeamsViewHolder(myView: View): RecyclerView.ViewHolder(myView) {
        var teamTitle: TextView = myView.findViewById(R.id.teams_title)
        var deleteTeam: TextView = myView.findViewById(R.id.delete_team_button)
        var seeMembers: TextView = myView.findViewById(R.id.see_members_button)

        fun initializeUIComponents(_title: String){
            teamTitle.text = _title

        }
    }

    //Method used to change the color of an item depending on the position
    private fun colorElement(card: View, position: Int) {
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
            listenerFragment.seeMembers(arrayList[position], position)
        }
    }

    //Method used to delete a team
    private fun deleteTeam(button: TextView, position: Int) {
        button.setOnClickListener {
            listenerFragment.deleteTeamFromAdapter(position)
        }
    }
}