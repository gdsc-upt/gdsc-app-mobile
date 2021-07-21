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
import com.example.gdsc_app_mobile.activities.MainActivity
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
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


class DialogFragmentAddEvent : DialogFragment() {

    lateinit var listener: ISelectedEvent
    lateinit var imageUri: Uri

    private var _eventBinding: CalendarDialogAddEventBinding? = null
    private val eventBinding get() = _eventBinding!!

    private val startForEventImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                //val imageUri = data?.data!!
                if (data != null) {
                    imageUri = data.data!!
                }

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
            UploadImageUploadEvent(eventBinding) //upload img and get response body path to add to event model it coresponds to
            dialog?.dismiss()
            Toast.makeText(requireContext(), resources.getString(R.string.event_posted), Toast.LENGTH_SHORT).show()
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

    private fun UploadImageUploadEvent(eventBinding: CalendarDialogAddEventBinding) {
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
                    .url("https://dev.api.dscupt.tech/api/v1/files")
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

                    createPostEvent(files[0], eventBinding)

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

    private fun createPostEvent(file : FileModel, eventBinding: CalendarDialogAddEventBinding) {

        val dateFrom = LocalDate.of(
            eventBinding.dateYear.text.toString().toInt(),
            eventBinding.dateMonth.text.toString().toInt(),
            eventBinding.dateDay.text.toString().toInt()
        ).atTime(
            eventBinding.fromHour.text.toString().toInt(),
            eventBinding.fromMins.text.toString().toInt(),
            10
        )

        val dateTo = LocalDate.of(
            eventBinding.dateYear.text.toString().toInt(),
            eventBinding.dateMonth.text.toString().toInt(),
            eventBinding.dateDay.text.toString().toInt()
        ).atTime(
            eventBinding.toHour.text.toString().toInt(),
            eventBinding.toMins.text.toString().toInt(),
            10
        )

        val newEvent = EventModel(
            eventBinding.eventTitleText.text.toString(),
            eventBinding.eventDescriptionText.text.toString(),
            file.id,
            "$dateFrom+03:00",
            "$dateTo+03:00"
        )

        Log.i("INFOOO", newEvent.toString())

        val eventCall: Call<EventModel> = ApiClient.getService()
            .postEvent(Singleton.getTokenForAuthentication().toString(), newEvent)

        eventCall.enqueue(object : Callback<EventModel> {

            override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                if (response.isSuccessful) {
                    Log.i("WORKIMG", response.raw().toString())

                } else {
                    Log.e("ERR", response.raw().toString())
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