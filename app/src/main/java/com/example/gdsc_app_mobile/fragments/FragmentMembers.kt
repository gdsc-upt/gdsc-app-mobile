package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.models.TeamsModel

//This fragment is used to show the list of members for each team(when a fragment like this starts, a team is required)
class FragmentMembers(var team: TeamsModel) : Fragment() {

    lateinit var tvTeamTitle: TextView
    lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_members, container, false)

        tvTeamTitle = view.findViewById(R.id.members_team_title)
        backButton = view.findViewById(R.id.button_members_back_to_teams)

        tvTeamTitle.text = team.name

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment, FragmentTeams())      //New fragment
                .commit()
            requireActivity().supportFragmentManager.popBackStack()     //Because there was not used back button, this fragment will remain in the stack, so we need to remove it programmatically
        }

        return view
    }
}