package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.TeamsAdapter
import com.example.gdsc_app_mobile.interfaces.ISelectedDataTeams
import com.example.gdsc_app_mobile.models.MemberModel
import com.example.gdsc_app_mobile.models.TeamsModel
import com.example.gdsc_app_mobile.services.ApiClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTeams : Fragment(), ISelectedDataTeams {

    private lateinit var teams: ArrayList<TeamsModel>
    private lateinit var allMembers: ArrayList<MemberModel>

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
                            teams.add(team)                                                 //Adding to the list all the teams read from backend
                    val adapter = TeamsAdapter(requireActivity(), teams)
                    adapter.addListener(this@FragmentTeams)                          //This is used to tell the adapter who is the listener
                    val listView: ListView = view!!.findViewById(R.id.teams_list_view)
                    listView.divider = null                                                 //No horizontal line between items
                    listView.adapter = adapter                                              //Set the adapter for the listview

                    getAllMembers()
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<TeamsModel>>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })

    }

    //This method is used from TeamsAdapter to start the Members fragment for another team
    //This will replace the container from the MainActivity with the new Fragment
    override fun seeMembers(team: TeamsModel, position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, FragmentMembers(team, allMembers, position))        //The new Fragment
            .addToBackStack(null)                                                 //Adding the fragment to the stack (after the fragment starts, if we press back, we'll be redirected to the previous fragment)
            .commit()
    }

    //This method get all the members from backend and put them in allMembers list
    private fun getAllMembers() {

        allMembers = ArrayList()

        val memberCall: Call<List<MemberModel>> = ApiClient.getService().getMembers()

        memberCall.enqueue(object : Callback<List<MemberModel>> {
            override fun onResponse(
                call: Call<List<MemberModel>>,
                response: Response<List<MemberModel>>
            ) {
                if(response.isSuccessful) {
                    val members : List<MemberModel>? = response.body()
                    if(members != null)
                        for(member in members)
                            allMembers.add(member)
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<MemberModel>>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })
    }
}