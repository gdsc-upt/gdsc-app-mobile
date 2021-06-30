package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.MembersAdapter
import com.example.gdsc_app_mobile.models.MemberModel
import com.example.gdsc_app_mobile.models.TeamsModel

//This fragment is used to show the list of members for each team(when a fragment like this starts, a team is required)
class FragmentMembers() : Fragment() {

    lateinit var team: TeamsModel
    lateinit var allMembers: ArrayList<MemberModel>
    lateinit var tvTeamTitle: TextView
    var position: Int = 0
    lateinit var backButton: Button
    lateinit var teamMembers: ArrayList<MemberModel>

    constructor(team: TeamsModel, allMembers: ArrayList<MemberModel>, position: Int) : this() {
        this.team = team
        this.allMembers = allMembers
        this.position = position
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val toolbar = (activity as MainActivity).toolbar
        HelperClass.setToolbarStyle(context, toolbar, "teams")

        val view = inflater.inflate(R.layout.fragment_members, container, false)

        tvTeamTitle = view.findViewById(R.id.members_team_title)
        backButton = view.findViewById(R.id.button_members_back_to_teams)

        tvTeamTitle.text = team.name    //Setting the name of the team

        colorPage(tvTeamTitle)  //Coloring the header of the page

        backButtonCLicked()     //Back button click listener

        setListView(view)       //Displaying only the members of this team

        return view
    }

    private fun setListView(view: View) {

        teamMembers = ArrayList()
        for(member in allMembers)
            if(team.id == member.teamId)
                teamMembers.add(member)     //Adding only the members with the same teamId as the ID of the team

        val adapter = MembersAdapter(requireActivity(), teamMembers)
        adapter.position = position     //We set the position of the team in the list (for coloring)
        val listView : ListView = view.findViewById(R.id.members_list_view)
        listView.adapter = adapter

        listView.divider = null     //No horizontal line between items
    }

    private fun colorPage(card: View) {
        when(position % 4) {
            0 -> card.setBackgroundColor(requireActivity().getColor(R.color.gdsc_yellow))
            1 -> card.setBackgroundColor(requireActivity().getColor(R.color.gdsc_red))
            2 -> card.setBackgroundColor(requireActivity().getColor(R.color.gdsc_green))
            3 -> card.setBackgroundColor(requireActivity().getColor(R.color.gdsc_blue))
        }
    }

    private fun backButtonCLicked() {
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment, FragmentTeams())      //New fragment
                .commit()
            requireActivity().supportFragmentManager.popBackStack()     //Because there was not used back button, this fragment will remain in the stack, so we need to remove it programmatically
        }
    }

}