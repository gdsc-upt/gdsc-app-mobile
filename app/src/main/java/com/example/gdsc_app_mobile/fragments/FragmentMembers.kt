package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.models.MemberModel
import com.example.gdsc_app_mobile.models.TeamsModel
import com.example.gdsc_app_mobile.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//This fragment is used to show the list of members for each team(when a fragment like this starts, a team is required)
class FragmentMembers(var team: TeamsModel) : Fragment() {

    lateinit var tvTeamTitle: TextView
    lateinit var backButton: Button
    lateinit var allMembers: ArrayList<MemberModel>

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

        getAllMembers()

        return view
    }

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
                    for(member in allMembers)
                        println(member.name)
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