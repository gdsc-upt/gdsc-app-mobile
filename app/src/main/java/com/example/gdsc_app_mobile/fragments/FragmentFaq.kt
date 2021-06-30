package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.HelperClass
import com.example.gdsc_app_mobile.services.ApiClient
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.Singleton
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.RVAdapterFaq
import com.example.gdsc_app_mobile.dialogs.DialogFaqFragmentDeleteQuestion
import com.example.gdsc_app_mobile.dialogs.DialogFragmentFaqAddQuestion
import com.example.gdsc_app_mobile.interfaces.ISelectedData
import com.example.gdsc_app_mobile.interfaces.OnItemClickListener
import com.example.gdsc_app_mobile.models.FaqModel
import com.example.gdsc_app_mobile.models.FaqPostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class FragmentFaq : Fragment(), ISelectedData, OnItemClickListener {

    private lateinit var addFaqButton: Button
    private lateinit var faqs: ArrayList<FaqModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvFaqAdapter: RVAdapterFaq

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_faq, container, false)
        val toolbar = (activity as MainActivity).toolbar
        HelperClass.setToolbarStyle(context, toolbar, "faq")

        addFaqButton = view.findViewById(R.id.add_faq_button)

        // initialize recycler view
        recyclerView = view.findViewById(R.id.faq_recycler_view)
        rvFaqAdapter = RVAdapterFaq(activity as MainActivity, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = rvFaqAdapter

        adminRole()

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
                    faqList?.let { rvFaqAdapter.setFaq(it) }
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

        val faqCall : Call<FaqModel> = ApiClient.getService().postFaq(Singleton.getTokenForAuthentication().toString() ,faq)

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

    private fun detailedFaq(faq: FaqModel?) {
        val dialog = DialogFaqFragmentDeleteQuestion()
        dialog.addListener(this)
        faq?.let { dialog.setFaq(it) }
        dialog.show(requireActivity().supportFragmentManager, "DeleteQuestionDialog")
    }

    override fun deleteFaq(faq: FaqModel?) {

        val id = faq?.id

        val deleteFaqCall = ApiClient.getService().deleteFaq(Singleton.getTokenForAuthentication().toString(), id)

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

    private fun adminRole() {
        if(Singleton.getTokenInfo() != null) {
            /*if (Singleton.getTokenInfo()?.roles.equals("admin"))
                addFaqButton.visibility = View.VISIBLE
            else
                addFaqButton.visibility = View.GONE*/
        }
        else
            //Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show()
            addFaqButton.visibility = View.GONE
    }

    override fun onItemClick(faq: FaqModel?) {
        detailedFaq(faq)
    }

    override fun onLongItemClick(faq: FaqModel?) {
        Toast.makeText(requireContext(), "long", Toast.LENGTH_SHORT).show()
    }
}