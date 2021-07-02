package com.example.gdsc_app_mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.animations.Animate
import com.example.gdsc_app_mobile.interfaces.OnItemClickListener
import com.example.gdsc_app_mobile.models.FaqModel


class RVAdapterFaq(var context: Context, var listener: OnItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var arrayList = emptyList<FaqModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.card_view_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun setFaq(list: List<FaqModel>){
        this.arrayList = list
        notifyDataSetChanged()
    }

    fun getFaqList(): List<FaqModel>{
        return this.arrayList
    }

    fun getFaq(position: Int): FaqModel {
        return arrayList[position]
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val type : FaqModel = arrayList[position]
        (holder as FaqViewHolder)
            .initializeUIComponents(type.question, type.answer)
        holder.itemView.setOnClickListener{
            listener.onItemClick(type)
            val show = toggleLayout(!type.isExpanded, holder.viewMore, holder.layoutExpand)
            type.isExpanded = show
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongItemClick(type)
            true
        }

    }
    // Holder that initialize elements inside the card view
    inner class FaqViewHolder(myView: View) : RecyclerView.ViewHolder(myView){
        var question: TextView = myView.findViewById(R.id.card_view_faq_question)
        var viewMore : ImageView = myView.findViewById(R.id.card_view_faq_view_more)
        var layoutExpand: LinearLayout = myView.findViewById(R.id.card_view_faq_layout_expand)
        var expandedText: TextView = myView.findViewById(R.id.card_view_faq_expanded_text)

        fun initializeUIComponents(_question: String, _answer: String){
            question.text = _question
            expandedText.text = _answer

        }
    }

    private fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: LinearLayout): Boolean {
        Animate.toggleArrow(v, isExpanded)
        if (isExpanded) {
            Animate.expand(layoutExpand);
        } else {
            Animate.collapse(layoutExpand);
        }
        return isExpanded
    }
}