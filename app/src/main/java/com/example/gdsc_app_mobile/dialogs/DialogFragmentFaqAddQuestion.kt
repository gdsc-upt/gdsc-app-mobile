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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.interfaces.ISelectedData

class DialogFragmentFaqAddQuestion: DialogFragment() {

    private lateinit var faqAddQuestion: EditText
    private lateinit var faqAddAnswer: EditText
    private lateinit var faqAddButton: Button
    private lateinit var faqAddError: TextView

    lateinit var listener: ISelectedData

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_faq_post, container,false)
        //set background transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        faqAddQuestion = view.findViewById(R.id.faq_add_question)
        faqAddAnswer = view.findViewById(R.id.faq_add_answer)
        faqAddButton = view.findViewById(R.id.faq_add_button)
        faqAddError = view.findViewById(R.id.faq_add_error)

        // check if the edit texts are blank, else pass the data to the fragment through onSelectedData interface
        faqAddButton.setOnClickListener {
            when {
                faqAddQuestion.text.toString() == "" -> faqAddError.text = resources.getString(R.string.no_question)
                faqAddAnswer.text.toString() == "" -> faqAddError.text = resources.getString(R.string.no_answer)
                else -> {
                    listener.onSelectedData(faqAddQuestion.text.toString(), faqAddAnswer.text.toString())
                    dialog?.dismiss()
                }
            }
        }
        return view
    }

    //method to add the listener from the fragment in which the dialog is called
    fun addListener(listener: ISelectedData){
        this.listener = listener
    }
}