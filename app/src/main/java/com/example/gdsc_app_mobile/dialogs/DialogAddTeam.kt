package com.example.gdsc_app_mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.interfaces.ISelectedDataTeams

class DialogAddTeam : DialogFragment() {

    private lateinit var teamName: EditText
    private lateinit var addTeamButton : Button

    lateinit var listener : ISelectedDataTeams

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_teams_post, container,false)
        //set background transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        teamName = view.findViewById(R.id.team_name)
        addTeamButton = view.findViewById(R.id.team_add_button)

        addTeamButton.setOnClickListener {
            when {
                teamName.text.toString() == "" -> resources.getString(R.string.no_team)
                else -> {
                    listener.postTeamFromDialog(teamName.text.toString())
                    dialog?.dismiss()
                }
            }
        }

        return view
    }
}