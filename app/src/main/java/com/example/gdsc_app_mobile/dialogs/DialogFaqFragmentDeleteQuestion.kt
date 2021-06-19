package com.example.gdsc_app_mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.interfaces.ISelectedData
import com.example.gdsc_app_mobile.models.FaqModel

class DialogFaqFragmentDeleteQuestion : DialogFragment() {

    private lateinit var faqDeleteButton: Button
    private lateinit var faqDeleteNo: Button
    private lateinit var faqDeleteYes: Button
    private lateinit var faqViewQuestion: TextView
    private lateinit var faqViewDate: TextView
    private lateinit var faqViewAnswer: TextView
    private lateinit var faqDeleteMessage: TextView

    private lateinit var listener : ISelectedData
    private lateinit var faq: FaqModel
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.detailed_faq, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        faqViewQuestion = view.findViewById(R.id.faq_view_question)
        faqViewAnswer = view.findViewById(R.id.faq_view_answer)
        faqViewDate = view.findViewById(R.id.faq_view_date)
        faqDeleteButton = view.findViewById(R.id.faq_delete_button)
        faqDeleteNo = view.findViewById(R.id.faq_delete_no)
        faqDeleteYes = view.findViewById(R.id.faq_delete_yes)
        faqDeleteMessage = view.findViewById(R.id.faq_delete_message)

        faqViewQuestion.text = faq.question
        faqViewAnswer.text = faq.answer
        faqViewDate.text = getDate(faq.created)

        adminRole()

        faqDeleteButtonClicked()

        return view
    }

    private fun getDate(date: String?): String {
        val year = date?.substring(0, 4)
        val month = date?.substring(5, 7)
        val day = date?.substring(8, 10)

        return "$day/$month/$year"
    }

    fun addListener(listener: ISelectedData){
        this.listener = listener
    }

    fun setFaq(_faq: FaqModel){
        this.faq = _faq
    }

    fun setPosition(_position: Int){
        this.position = _position
    }

    private fun adminRole() {
        if(Singleton.getTokenInfo() != null) {
            if (Singleton.getTokenInfo()?.roles.equals("admin"))
                faqDeleteButton.visibility = View.VISIBLE
            else
                faqDeleteButton.visibility = View.GONE
        }
        else
            faqDeleteButton.visibility = View.GONE
    }

    private fun faqDeleteButtonClicked() {
        faqDeleteButton.setOnClickListener {
            faqDeleteMessage.text = resources.getString(R.string.sure_want_delete)
            faqDeleteButton.visibility = View.GONE
            faqDeleteNo.visibility = View.VISIBLE
            faqDeleteYes.visibility = View.VISIBLE

            faqDeleteYes.setOnClickListener {
                listener.deletePosition(position)
                dialog?.dismiss()
            }
            faqDeleteNo.setOnClickListener {
                faqDeleteMessage.text = ""
                faqDeleteButton.visibility = View.VISIBLE
                faqDeleteNo.visibility = View.GONE
                faqDeleteYes.visibility = View.GONE
            }
        }
    }
}