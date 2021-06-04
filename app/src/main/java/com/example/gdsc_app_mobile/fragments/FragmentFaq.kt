package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.services.ApiClient
import com.example.gdsc_app_mobile.adapters.FaqAdapter
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.dialogs.DialogFragmentFaqAddQuestion
import com.example.gdsc_app_mobile.interfaces.ISelectedData
import com.example.gdsc_app_mobile.models.FaqModel
import com.example.gdsc_app_mobile.models.FaqPostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class FragmentFaq : Fragment(), ISelectedData {

    private lateinit var faqDeleteButton: Button
    private lateinit var faqDeleteNo: Button
    private lateinit var faqDeleteYes: Button
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
        (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title).text = getString(
                    R.string.FragmentFaqTitle)

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
        val dialog = DialogFragmentFaqAddQuestion()
        // Add a listener to listen to the data from the dialog
        dialog.addListener(this)
        dialog.show(requireActivity().supportFragmentManager, "AddQuestionDialog")
    }

    private fun createPost(_question: String, _answer: String) {

        val faq = FaqPostModel(
            question = _question,
            answer = _answer
        )

        val faqCall : Call<FaqModel> = ApiClient.getService().postFaq("Bearer ${Singleton.getToken()}" ,faq)

        faqCall.enqueue(object : Callback<FaqModel>{

            override fun onResponse(call: Call<FaqModel>, response: Response<FaqModel>) {
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
                Toast.makeText(requireContext(), resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()

            }
        })
    }

    fun detailedFaq(position: Int) {
//        dialogBuilder = AlertDialog.Builder(context)
        val faqDetailsView = layoutInflater.inflate(R.layout.detailed_faq, null)

        faqViewQuestion = faqDetailsView.findViewById(R.id.faq_view_question)
        faqViewAnswer = faqDetailsView.findViewById(R.id.faq_view_answer)
        faqViewDate = faqDetailsView.findViewById(R.id.faq_view_date)
        faqDeleteButton = faqDetailsView.findViewById(R.id.faq_delete_button)
        faqDeleteNo = faqDetailsView.findViewById(R.id.faq_delete_no)
        faqDeleteYes = faqDetailsView.findViewById(R.id.faq_delete_yes)
        faqDeleteMessage = faqDetailsView.findViewById(R.id.faq_delete_message)

        faqViewQuestion.text = faqs[position].question
        faqViewAnswer.text = faqs[position].answer
        faqViewDate.text = getDate(faqs[position].created)

//        dialogBuilder.setView(faqDetailsView)
//        dialog = dialogBuilder.create()
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//        dialog.show()

        faqDeleteButton.setOnClickListener {
            faqDeleteMessage.text = resources.getString(R.string.sure_want_delete)
            faqDeleteButton.visibility = View.GONE
            faqDeleteNo.visibility = View.VISIBLE
            faqDeleteYes.visibility = View.VISIBLE

            faqDeleteYes.setOnClickListener {
                deleteFaq(position)
            }
            faqDeleteNo.setOnClickListener {
                faqDeleteMessage.text = ""
                faqDeleteButton.visibility = View.VISIBLE
                faqDeleteNo.visibility = View.GONE
                faqDeleteYes.visibility = View.GONE
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

    // This method is called in AddQuestion Dialog
    override fun onSelectedData(question: String, answer: String) {
        Toast.makeText(context, "Question: $question Answer: $answer",Toast.LENGTH_LONG).show()
        //HERE YOU SHOULD CALL CREATE POST method
        //createPost(question, answer)
    }
}