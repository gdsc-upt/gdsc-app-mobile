package com.example.gdsc_app_mobile.dialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.URIPathHelper
import com.example.gdsc_app_mobile.databinding.CalendarDialogAddEventBinding
import com.example.gdsc_app_mobile.interfaces.ISelectedEvent
import com.example.gdsc_app_mobile.models.EventModel
import com.example.gdsc_app_mobile.models.FileModel
import com.example.gdsc_app_mobile.services.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class DialogFragmentAddEvent : DialogFragment() {

    lateinit var listener: ISelectedEvent
    lateinit var imageUri: Uri
    lateinit var file: FileModel
    lateinit var newEvent: EventModel

    private var _eventBinding: CalendarDialogAddEventBinding? = null
    private val eventBinding get() = _eventBinding!!

    private val startForEventImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                imageUri = fileUri

                Picasso.get().load(imageUri).transform(CropCircleTransformation())
                    .into(eventBinding.eventImage)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _eventBinding = CalendarDialogAddEventBinding.inflate(inflater, container, false)
        val view = eventBinding.root
        //set background transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        eventBinding.eventImage.setOnClickListener {
            ImagePicker.with(this).compress(1024).maxResultSize(1080, 1080)
                .createIntent { intent -> startForEventImageResult.launch(intent) }
        }

        eventBinding.eventAddButton.setOnClickListener {//to be worked on
            UploadImage() //upload img and get response body path to add to event model it coresponds to
            //assembleEventModel()
            //createPostEvent()
            dialog?.dismiss()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _eventBinding = null
    }

    //method to add the listener from the fragment in which the dialog is called
    fun addListener(listener: ISelectedEvent) {
        this.listener = listener
    }

    private fun assembleEventModel() {

        val date: String =
            eventBinding.dateYear.text.toString() + "-" + eventBinding.dateMonth.text.toString() + "-" + eventBinding.dateDay.text.toString() + "T"
        val from: String =
            eventBinding.fromHour.text.toString() + ":" + eventBinding.fromMins.text.toString() + ".+03:00"
        val to: String =
            eventBinding.toHour.text.toString() + ":" + eventBinding.toMins.text.toString() + ".+03:00"

        newEvent = EventModel(
            eventBinding.eventTitleText.text.toString(),
            eventBinding.eventDescriptionText.text.toString(),
            file.id,
            date + from,
            date + to
        )
    }

    private fun UploadImage() {
        Thread {
            val file = File(URIPathHelper().getPath(requireActivity(), imageUri))
            try {
                Log.i("FILE NAME", file.name)

                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files", file.name, file.asRequestBody(MultipartBody.FORM))
                        .build()
                Log.i("PLM", requestBody.toString())

                val request: okhttp3.Request = okhttp3.Request.Builder()
                    .url("https://api.gdscupt.tech/api/v1/files")
                    .header("Authorization", Singleton.getTokenForAuthentication().toString())
                    .post(requestBody)
                    .build()

                val response: okhttp3.Response = OkHttpClient().newCall(request).execute()
                Log.i("WTF", request.headers.toString())
                if (response.isSuccessful) {
                    Log.i("MERE", response.code.toString())
                    val body = response.body?.string()
                    if (body != null) {
                        Log.i("MERE", body)
                    } else {
                        Log.e("OF", "null body")
                    }
                    val gson = Gson()
                    val files =
                        gson.fromJson(body ?: """[{"err": "plm"}]""", Array<FileModel>::class.java)
                    Log.i("MERE", files.size.toString())
                } else {
                    Log.e("CPLM", response.message)
                    Log.e("CPLM", response.code.toString())
                    Log.e("CPLM", response.body.toString())
                    Log.e("CPLM", response.headers.toString())
                }
            } catch (e: Exception) {
                Log.e("ERR", e.message.toString())
                Log.e("ERR", e.stackTraceToString())
                return@Thread
            }
        }.start()
    }

    private fun createPostEvent() {

        val eventCall: Call<EventModel> = ApiClient.getService()
            .postEvent(Singleton.getTokenForAuthentication().toString(), newEvent)

        eventCall.enqueue(object : Callback<EventModel> {

            override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.faq_successfully_posted),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.something_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<EventModel>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.something_wrong),
                    Toast.LENGTH_SHORT
                ).show()

            }
        })

    }

}