package com.example.gdsc_app_mobile.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.services.ApiClient
import com.example.gdsc_app_mobile.adapters.FaqAdapter
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.models.FaqModel
import com.example.gdsc_app_mobile.models.FaqPostModel
import org.w3c.dom.Text
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
    private lateinit var faqAddError: TextView

    private lateinit var faqDeleteButton: Button
    private lateinit var faqViewQuestion: TextView
    private lateinit var faqViewDate: TextView
    private lateinit var faqViewAnswer: TextView
    private lateinit var faqDeleteMessage: TextView


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

    fun getFaqs() {
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
                        detailedFaq(position)
                    }
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<FaqModel>>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addFaq() {
        dialogBuilder = AlertDialog.Builder(context)
        val faqAddPopUp = layoutInflater.inflate(R.layout.add_faq_pop_up, null)

        faqAddQuestion = faqAddPopUp.findViewById(R.id.faq_add_question)
        faqAddAnswer = faqAddPopUp.findViewById(R.id.faq_add_answer)
        faqAddButton = faqAddPopUp.findViewById(R.id.faq_add_button)
        faqAddError = faqAddPopUp.findViewById(R.id.faq_add_error)

        dialogBuilder.setView(faqAddPopUp)
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()

        faqAddButton.setOnClickListener {
            when {
                faqAddQuestion.text.toString() == "" -> faqAddError.text = resources.getString(R.string.no_question)
                faqAddAnswer.text.toString() == "" -> faqAddError.text = resources.getString(R.string.no_answer)
                else -> createPost()
            }
        }
    }

    private fun createPost() {

        val faq = FaqPostModel(
            question = faqAddQuestion.text.toString(),
            answer = faqAddAnswer.text.toString()
        )

        val faqCall : Call<FaqModel> = ApiClient.getService().postFaq("Bearer ${Singleton.getToken()}" ,faq)

        faqCall.enqueue(object : Callback<FaqModel>{
            override fun onResponse(call: Call<FaqModel>, response: Response<FaqModel>) {
                dialog.dismiss()
                if(response.isSuccessful){
                    faqs = ArrayList()
                    getFaqs()
                    Toast.makeText(requireContext(), resources.getString(R.string.faq_successfully_posted), Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FaqModel>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()

            }
        })
    }

    fun detailedFaq(position: Int) {
        dialogBuilder = AlertDialog.Builder(context)
        val faqDetailsView = layoutInflater.inflate(R.layout.detailed_faq, null)

        faqViewQuestion = faqDetailsView.findViewById(R.id.faq_view_question)
        faqViewAnswer = faqDetailsView.findViewById(R.id.faq_view_answer)
        faqViewDate = faqDetailsView.findViewById(R.id.faq_view_date)
        faqDeleteButton = faqDetailsView.findViewById(R.id.faq_delete_button)
        faqDeleteMessage = faqDetailsView.findViewById(R.id.faq_delete_message)

        faqViewQuestion.text = faqs[position].question
        faqViewAnswer.text = faqs[position].answer
        faqViewDate.text = getDate(faqs[position].created)

        dialogBuilder.setView(faqDetailsView)
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()

        faqDeleteButton.setOnClickListener {
            faqDeleteMessage.text = resources.getString(R.string.sure_want_delete)
            faqDeleteButton.text = resources.getString(R.string.yes)
            faqDeleteButton.setOnClickListener {
                deleteFaq(position)
            }
        }
    }

    private fun getDate(date: String?): String {
        val year = date?.substring(0, 4)
        val month = date?.substring(5, 7)
        val day = date?.substring(8, 10)

        return "$day/$month/$year"
    }

    private fun deleteFaq(position: Int) {

        val id = faqs[position].id

        val deleteFaqCall = ApiClient.getService().deleteFaq("Bearer ${Singleton.getToken()}", id)

        deleteFaqCall.enqueue(object : Callback<FaqModel>{
            override fun onResponse(call: Call<FaqModel>, response: Response<FaqModel>) {
                dialog.dismiss()
                if(response.isSuccessful){
                    faqs = ArrayList()
                    getFaqs()
                }
                else
                    Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(call: Call<FaqModel>, t: Throwable) {
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
            }

        })
    }
}