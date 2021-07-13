package com.example.gdsc_app_mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.dialogs.DialogAccept
import com.example.gdsc_app_mobile.interfaces.ISelectedAccept
import com.example.gdsc_app_mobile.interfaces.ISelectedDataMembers
import com.example.gdsc_app_mobile.interfaces.OnItemClickListener
import com.example.gdsc_app_mobile.models.MemberModel

class RVAdapterMembers(var context: Context, var listener: OnItemClickListener, var position: Int, var activity: FragmentActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>(), ISelectedAccept {

    private var arrayList = emptyList<MemberModel>()
    lateinit var listenerFragment: ISelectedDataMembers

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.card_view_member, parent, false)
        return MembersViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type : MemberModel = arrayList[position]          //Getting the specific member from array
        (holder as RVAdapterMembers.MembersViewHolder)
            .initializeUIComponents(type.name)

        HelperClass.adminRole(holder.deleteMember)

        colorElement(holder.itemView, this.position)

        deleteMember(holder.deleteMember)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class MembersViewHolder(myView: View): RecyclerView.ViewHolder(myView) {

        var memberName: TextView = myView.findViewById(R.id.member_name)
        var deleteMember: ImageView = myView.findViewById(R.id.delete_member)

        fun initializeUIComponents(_name: String){
            memberName.text = _name
        }
    }

    fun setMember(list: List<MemberModel>){
        this.arrayList = list
        notifyDataSetChanged()
    }

    private fun colorElement(card: View, position: Int) {
        when(position % 4) {
            0 -> card.setBackgroundColor(context.getColor(R.color.gdsc_yellow))
            1 -> card.setBackgroundColor(context.getColor(R.color.gdsc_red))
            2 -> card.setBackgroundColor(context.getColor(R.color.gdsc_green))
            3 -> card.setBackgroundColor(context.getColor(R.color.gdsc_blue))
        }
    }

    private fun deleteMember(button: View) {
        button.setOnClickListener{
            val dialog = DialogAccept("Are you sure you want to delete this member?")
            dialog.position = position
            dialog.listener = this
            dialog.show(activity.supportFragmentManager, "AcceptDialog")
        }
    }

    override fun acceptYes(position: Int) {
        listenerFragment.deleteMember(position)
    }
}