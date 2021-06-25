package com.example.gdsc_app_mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R

class DialogCloseApp: DialogFragment() {

    private lateinit var answerNo: Button
    private lateinit var answerYes: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_close_app, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))     //Transparent background
        dialog?.setCancelable(false)                                                //Dialog not dismissed on back pressed
        dialog?.setCanceledOnTouchOutside(false)                                    //Dialog not dismissed on outside touch

        answerNo = view.findViewById(R.id.close_delete_no)
        answerYes = view.findViewById(R.id.close_delete_yes)

        answerNo.setOnClickListener {
            dialog!!.dismiss()                                    //If the answer is NO, the dialog box must be closed
        }

        answerYes.setOnClickListener {
            requireActivity().finishAndRemoveTask()               //If the answer is YES, the app will close completely
        }

        return view
    }
}