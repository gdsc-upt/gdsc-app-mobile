package com.example.gdsc_app_mobile.dialogs

import android.app.Activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gdsc_app_mobile.fragments.FragmentHomePage
import com.example.gdsc_app_mobile.interfaces.ISelectedEvent
import com.github.dhaval2404.imagepicker.ImagePicker


class DialogFragmentAddEvent: DialogFragment() {

    lateinit var listener: ISelectedEvent
    lateinit var image: ImageView
    lateinit var addEvent: Button
    lateinit var title: EditText
    lateinit var description: EditText
    lateinit var imageUri : Uri

    private val startForEventImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                imageUri = fileUri
                image.setImageURI(fileUri)
            }
        }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_add_event, container,false)
        //set background transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        image = view.findViewById(R.id.event_image)
        title = view.findViewById(R.id.event_title_text)
        description = view.findViewById(R.id.event_description_text)
        addEvent = view.findViewById(R.id.event_add_button)

        image.setOnClickListener{
            ImagePicker.with(this).compress(1024).maxResultSize(1080, 1080).
                createIntent { intent -> startForEventImageResult.launch(intent) }
        }

        addEvent.setOnClickListener {
            listener.onSelectedEvent(title.text.toString(), description.text.toString(), imageUri.toString())
            dialog?.dismiss()
        }

        return view
    }

    //method to add the listener from the fragment in which the dialog is called
    fun addListener(listener: ISelectedEvent){
        this.listener = listener
    }

}