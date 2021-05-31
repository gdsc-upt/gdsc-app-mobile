package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gdsc_app_mobile.services.ApiClient
import com.example.gdsc_app_mobile.adapters.FaqAdapter
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.models.FaqModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFaq : Fragment() {

    //private lateinit var faqList: ArrayList<FaqModel>
    private var faqs = ArrayList<FaqModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_faq, container, false)

        getFaqs()



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
}