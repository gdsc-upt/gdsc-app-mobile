package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.TeamsAdapter
import com.example.gdsc_app_mobile.models.TeamsModel
import com.example.gdsc_app_mobile.services.ApiClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTeams : Fragment() {

    private lateinit var teams: ArrayList<TeamsModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_teams, container, false)
        (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title).text = getString(
                    R.string.FragmentTeamsTitle)

        getTeams()      //Getting the teams from backend

        return view
    }

    //This method is used to get the teams from backend
    //This also display the list of teams using listview
    fun getTeams() {
        val teamsCall : Call<List<TeamsModel>> = ApiClient.getService().getTeams()
        teams = ArrayList()

        teamsCall.enqueue(object : Callback<List<TeamsModel>>{
            override fun onResponse(
                call: Call<List<TeamsModel>>,
                response: Response<List<TeamsModel>>
            ) {
                if(response.isSuccessful) {
                    val teamsList: List<TeamsModel>? = response.body()
                    if(teamsList != null)
                        for(team in teamsList)
                            teams.add(team)         //Adding to the list all the teams read from backend
                    val adapter = TeamsAdapter(requireActivity(), teams)
                    val listView: ListView = view!!.findViewById(R.id.teams_list_view)
                    listView.divider = null         //No horizontal line between items
                    listView.adapter = adapter      //Set the adapter for the listview
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<TeamsModel>>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })

    }
}