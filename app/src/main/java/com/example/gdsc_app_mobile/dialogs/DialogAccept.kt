package com.example.gdsc_app_mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.interfaces.ISelectedAccept

//This dialog is used to show a confirmation question
//The constructor has a string variable that is used to fill de textView(the question)
class DialogAccept(var question: String) : DialogFragment() {

    lateinit var tvQuestion: TextView   //The textView will show the message
    lateinit var yesButton: Button      //Accept button
    lateinit var noButton: Button       //Reject button
    var position: Int = 0               //A position (This is used to delete a team (we need to specify position))

    lateinit var listener: ISelectedAccept  //The listener will apply the specific methods from the parent of the dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_accept, container, false)

        //Transparent background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Get views from layout
        tvQuestion = view.findViewById(R.id.accept_text_view)
        yesButton = view.findViewById(R.id.accept_yes)
        noButton = view.findViewById(R.id.accept_no)

        tvQuestion.text = question      //Set the text of the question

        //Yes button clicked
        yesButton.setOnClickListener {
            listener.acceptYes(position)        //We apply the specific method from the parent
            dialog?.dismiss()       //The dialog is closed
        }

        noButton.setOnClickListener {
            dialog?.dismiss()       //The dialog is closed
        }

        return view
    }
}