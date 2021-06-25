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
import com.example.gdsc_app_mobile.interfaces.ICloseApp

class DialogCloseApp: DialogFragment() {

    private lateinit var answerNo: Button
    private lateinit var answerYes: Button

    lateinit var listener: ICloseApp

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_close_app, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        answerNo = view.findViewById(R.id.close_delete_no)
        answerYes = view.findViewById(R.id.close_delete_yes)

        answerNo.setOnClickListener {
            dialog!!.dismiss()
        }

        answerYes.setOnClickListener {
            listener.closeApp()
        }

        return view
    }

    fun addListener(_listener: ICloseApp){
        this.listener = _listener
    }
}