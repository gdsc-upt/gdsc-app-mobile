package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.TeamsAdapter
import com.example.gdsc_app_mobile.interfaces.ISelectedDataTeams
import com.example.gdsc_app_mobile.models.TeamsModel
import com.example.gdsc_app_mobile.services.ApiClient
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.dialogs.DialogAccept
import com.example.gdsc_app_mobile.dialogs.DialogAddTeam
import com.example.gdsc_app_mobile.interfaces.ISelectedAccept
import com.example.gdsc_app_mobile.models.TeamsPostModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTeams : Fragment(), ISelectedDataTeams, ISelectedAccept {

    private lateinit var teams: ArrayList<TeamsModel>
    private lateinit var addTeamButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_teams, container, false)
        val toolbar = (activity as MainActivity).toolbar
        HelperClass.setToolbarStyle(context, toolbar, "teams")

        getTeams()      //Getting the teams from backend

        addTeamButton = view.findViewById(R.id.add_team_button)

        addTeam()       //This will show a dialog to add a new team

        HelperClass.adminRole(addTeamButton)     //This will display the ADD new team button only if the user is admin

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
                    listView.adapter = adapter
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
            .replace(R.id.container_fragment, FragmentMembers(team, position))        //The new Fragment
            .addToBackStack(null)                                                 //Adding the fragment to the stack (after the fragment starts, if we press back, we'll be redirected to the previous fragment)
            .commit()
    }

    //This method is used to POST a new team
    private fun postTeam(name: String) {
        val team = TeamsPostModel(name)
        val teamCall: Call<TeamsModel> = ApiClient.getService().postTeam(Singleton.getTokenForAuthentication().toString(), team)       //There the request is created

        //Request answers
        teamCall.enqueue(object : Callback<TeamsModel> {
            override fun onResponse(call: Call<TeamsModel>, response: Response<TeamsModel>) {       //If the request has a response
                if(response.isSuccessful){  //If the response is successful(The new team was posted successfully)
                    teams = ArrayList()
                    getTeams()
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(call: Call<TeamsModel>, t: Throwable) {                          //If there is no response from backend
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Delete team method
    private fun deleteTeam(position: Int) {
        val id = teams[position].id         //We get the specific ID for the team (We only need ID to delete it)

        val deleteTeamCall : Call<TeamsModel> = ApiClient.getService().deleteTeam(Singleton.getTokenForAuthentication().toString(), id)     //There the request is created

        //Request answers
        deleteTeamCall.enqueue(object : Callback<TeamsModel> {
            override fun onResponse(call: Call<TeamsModel>, response: Response<TeamsModel>) {       //If the request has a response
                if(response.isSuccessful){      //If the response is successful(The new team was posted successfully)
                    teams = ArrayList()
                    getTeams()
                    Toast.makeText(requireContext(), "${resources.getString(R.string.team)} \"${response.body()!!.name}\" ${resources.getString(R.string.team_has_been_deleted)}", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<TeamsModel>, t: Throwable) {                          //If there is no response from backend
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })
    }

    //This method is used to POST the new team from the dialog (The listener from there will apply this method to make the specific request)
    //We need to make the request from here because the list of teams have to be updated
    override fun postTeamFromDialog(name: String) {
        postTeam(name)
    }

    //This method is used to show the dialog for adding a new team
    private fun addTeam() {
        addTeamButton.setOnClickListener {
            val dialog = DialogAddTeam()
            // Add a listener to listen to the data from the dialog
            dialog.listener = this
            dialog.show(requireActivity().supportFragmentManager, "AddTeamDialog")
        }
    }

    //This method show a confirmation dialog for delete request
    override fun deleteTeamFromAdapter(position: Int) {
        val dialog = DialogAccept(resources.getString(R.string.delete_team_question))
        dialog.listener = this
        dialog.position = position
        dialog.show(requireActivity().supportFragmentManager, "AcceptDialog")
    }

    //If the answer from the previous dialog is yes, we need to delete the team from the specific position
    //This method is applied from DialogAccept if the YES button is clicked
    override fun acceptYes(position: Int) {
        deleteTeam(position)
    }
}