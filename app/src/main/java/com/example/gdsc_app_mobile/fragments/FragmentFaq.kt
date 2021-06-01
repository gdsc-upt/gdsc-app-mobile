package com.example.gdsc_app_mobile.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.services.ApiClient
import com.example.gdsc_app_mobile.adapters.FaqAdapter
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.models.FaqModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class FragmentFaq : Fragment() {

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var faqAddQuestion: EditText
    private lateinit var faqAddAnswer: EditText
    private lateinit var faqAddButton: Button
    private lateinit var faqCancelButton: Button
    private lateinit var faqAddError: TextView

    private lateinit var addFaqButton: Button
    private var faqs = ArrayList<FaqModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_faq, container, false)

        addFaqButton = view.findViewById(R.id.add_faq_button)


        getFaqs()

        addFaqButton.setOnClickListener {
            addFaq()
        }

        return view
    }

    private fun getFaqs() {
        val faqCall: Call<List<FaqModel>> = ApiClient.getService().getAllFaqs()

        faqCall.enqueue(object :Callback<List<FaqModel>>{
            override fun onResponse(
                call: Call<List<FaqModel>>,
                response: Response<List<FaqModel>>
            ) {
                if(response.isSuccessful){
                    val faqList: List<FaqModel>? = response.body()
                    if(faqList != null)
                        for(faq in faqList)
                            faqs.add(faq)
                    val adapter = FaqAdapter(requireActivity(), faqs)
                    val listView: ListView = view!!.findViewById(R.id.faq_list_view)
                    listView.adapter = adapter
                    listView.isClickable = true

                    listView.setOnItemClickListener { parent, view, position, id ->
                        Toast.makeText(requireContext(), "Position $position", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                    Toast.makeText(requireContext(), "Unsuccessful response", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<FaqModel>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addFaq() {
        dialogBuilder = AlertDialog.Builder(context)
        val faqAddPopUp = layoutInflater.inflate(R.layout.add_faq_pop_up, null)

        faqAddQuestion = faqAddPopUp.findViewById(R.id.faq_add_question)
        faqAddAnswer = faqAddPopUp.findViewById(R.id.faq_add_answer)
        faqAddButton = faqAddPopUp.findViewById(R.id.faq_add_button)
        faqCancelButton = faqAddPopUp.findViewById(R.id.faq_cancel_button)
        faqAddError = faqAddPopUp.findViewById(R.id.faq_add_error)

        dialogBuilder.setView(faqAddPopUp)
        dialog = dialogBuilder.create()
        dialog.setCancelable(false)
        dialog.show()

        faqCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        faqAddButton.setOnClickListener {
            if(faqAddQuestion.text.toString() == "")
                faqAddError.text = "No written question"
            else
                if(faqAddAnswer.text.toString() == "")
                    faqAddError.text = "No written answer"
            else {
                    createPost()
                    dialog.dismiss()
                }
        }
    }

    private fun createPost() {

        Toast.makeText(requireContext(), getCurrentDate(faqs[0].created), Toast.LENGTH_LONG).show()
    }

    private fun getCurrentDate(dateString: String): String {

        val year = dateString.substring(0, 4)
        val month = dateString.substring(5, 7)
        val day = dateString.substring(8,10)

        return "$day/$month/$year"

    }
}