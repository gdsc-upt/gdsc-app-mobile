package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.RVAdapterMembers
import com.example.gdsc_app_mobile.interfaces.ISelectedDataMembers
import com.example.gdsc_app_mobile.interfaces.OnItemClickListener
import com.example.gdsc_app_mobile.models.FaqModel
import com.example.gdsc_app_mobile.models.MemberModel
import com.example.gdsc_app_mobile.models.TeamsModel
import com.example.gdsc_app_mobile.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//This fragment is used to show the list of members for each team(when a fragment like this starts, a team is required)
class FragmentMembers() : Fragment(), ISelectedDataMembers, OnItemClickListener {

    lateinit var team: TeamsModel
    lateinit var allMembers: ArrayList<MemberModel>
    lateinit var tvTeamTitle: TextView
    var position: Int = 0
    lateinit var backButton: Button
    lateinit var teamMembers: ArrayList<MemberModel>
    lateinit var addMemberButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvMembersAdapter: RVAdapterMembers

    constructor(team: TeamsModel, position: Int) : this() {
        this.team = team
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
        addMemberButton = view.findViewById(R.id.add_member)

        // initialize recycler view
        recyclerView = view.findViewById(R.id.members_recycler_view)
        rvMembersAdapter = RVAdapterMembers(activity as MainActivity, this, position, requireActivity())
        rvMembersAdapter.listenerFragment = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = rvMembersAdapter

        HelperClass.adminRole(addMemberButton)

        tvTeamTitle.text = team.name    //Setting the name of the team

        colorPage(tvTeamTitle)  //Coloring the header of the page

        backButtonCLicked()     //Back button click listener

        getAllMembers()         //Get all members from backend

        addMemberClicked()      //Add member button functionality

        return view
    }

    //This method get all the members from backend and put them in allMembers list
    fun getAllMembers() {

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
                    setListView()
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<MemberModel>>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setListView() {

        teamMembers = ArrayList()
        for(member in allMembers)
            if(team.id == member.teamId)
                teamMembers.add(member)     //Adding only the members with the same teamId as the ID of the team

        teamMembers?.let { rvMembersAdapter.setMember(it) }     //Set the member list for recyclerView
    }

    //This is used to color the header of the page
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
            requireActivity().supportFragmentManager.popBackStack()     //Get back to teams fragment
        }
    }

    private fun addMemberClicked() {
        //TODO
    }

    fun getMemberId(position: Int): String{
        return allMembers[position].id
    }

    override fun deleteMember(position: Int) {
        val deleteMemberCall = ApiClient.getService().deleteMember(Singleton.getTokenForAuthentication().toString(), getMemberId(position))

        deleteMemberCall.enqueue(object : Callback<MemberModel> {
            override fun onResponse(call: Call<MemberModel>, response: Response<MemberModel>) {
                if(response.isSuccessful)
                    getAllMembers()
                else {
                    Toast.makeText(
                        requireContext(),
                        response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<MemberModel>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onItemClick(faq: FaqModel?) {}
    override fun onLongItemClick(faq: FaqModel?) {}

}