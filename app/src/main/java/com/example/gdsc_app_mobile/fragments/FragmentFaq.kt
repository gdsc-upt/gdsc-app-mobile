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
import com.example.gdsc_app_mobile.dialogs.DialogFaqFragmentDeleteQuestion
import com.example.gdsc_app_mobile.dialogs.DialogFragmentFaqAddQuestion
import com.example.gdsc_app_mobile.interfaces.ISelectedData
import com.example.gdsc_app_mobile.models.FaqModel
import com.example.gdsc_app_mobile.models.FaqPostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class FragmentFaq : Fragment(), ISelectedData {

    private lateinit var addFaqButton: Button
    private lateinit var faqs: ArrayList<FaqModel>

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
        faqs = ArrayList()

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

        val faqCall : Call<FaqModel> = ApiClient.getService().postFaq(Singleton.getToken().toString() ,faq)

        faqCall.enqueue(object : Callback<FaqModel>{

            override fun onResponse(call: Call<FaqModel>, response: Response<FaqModel>) {
                if(response.isSuccessful){
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

    fun detailedFaq(_position: Int) {
        val dialog = DialogFaqFragmentDeleteQuestion()
        dialog.addListener(this)
        dialog.setFaq(faqs[_position])
        dialog.setPosition(_position)
        dialog.show(requireActivity().supportFragmentManager, "DeleteQuestionDialog")
    }

    private fun deleteFaq(position: Int) {

        val id = faqs[position].id

        val deleteFaqCall = ApiClient.getService().deleteFaq(Singleton.getToken().toString(), id)

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
        createPost(question, answer)
    }

    override fun deletePosition(position: Int) {
        deleteFaq(position)
    }
}