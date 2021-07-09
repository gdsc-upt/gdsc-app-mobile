package com.example.gdsc_app_mobile.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.dialogs.DialogAccept
import com.example.gdsc_app_mobile.interfaces.ISelectedAccept
import com.example.gdsc_app_mobile.interfaces.ISelectedDataMembers
import com.example.gdsc_app_mobile.models.MemberModel
import java.util.ArrayList

class MembersAdapter(private val context: Activity, private val arrayList: ArrayList<MemberModel>): ArrayAdapter<MemberModel>(context,
    R.layout.card_member, arrayList), ISelectedAccept {

    var positionColoring: Int = 0       //Position needed for color

    lateinit var deleteMemberButton: ImageView
    lateinit var activity: FragmentActivity
    lateinit var listener: ISelectedDataMembers

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.card_member, null)

        val name: TextView = view.findViewById(R.id.member_name)
        deleteMemberButton = view.findViewById(R.id.delete_member)

        HelperClass.adminRole(deleteMemberButton)           //Delete members functionality only for admin

        colorElement(view.findViewById(R.id.member_card))   //Coloring the current item depending on position

        deleteMember(position)      //Delete a member if delete button is clicked and the response is YES

        name.text = "${position + 1}. ${arrayList[position].name}" //Setting the number and the name of the member

        return view
    }

    private fun colorElement(card: View) {
        when(positionColoring % 4) {
            0 -> card.setBackgroundColor(context.getColor(R.color.gdsc_yellow))
            1 -> card.setBackgroundColor(context.getColor(R.color.gdsc_red))
            2 -> card.setBackgroundColor(context.getColor(R.color.gdsc_green))
            3 -> card.setBackgroundColor(context.getColor(R.color.gdsc_blue))
        }
    }

    private fun deleteMember(position: Int) {
        deleteMemberButton.setOnClickListener {
            val dialog = DialogAccept("Are you sure you want to delete this member?")
            dialog.position = position
            dialog.listener = this
            dialog.show(activity.supportFragmentManager, "AcceptDialog")
        }
    }

    override fun acceptYes(position: Int) {
        listener.deleteMember(position)
    }

}