package com.example.gdsc_app_mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R

class DialogFragmentUserInfo: DialogFragment() {

    private lateinit var user_img: ImageView
    private lateinit var more_button: ImageView
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_user_info, container, false)
        //set background transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        user_img = view.findViewById(R.id.dialog_user_img)
        more_button = view.findViewById(R.id.dialog_user_more_button)
        user_img.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.on_click_animation_no_alpha))
        }
        more_button.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.on_click_animation_no_alpha))
        }

        return view
    }
}