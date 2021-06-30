package com.example.gdsc_app_mobile.interfaces

import com.example.gdsc_app_mobile.models.TeamsModel

interface ISelectedDataTeams {
    fun seeMembers(team: TeamsModel, position: Int)    //Communication between FragmentTeams and Teams Adapter ("See members" functionality)
}