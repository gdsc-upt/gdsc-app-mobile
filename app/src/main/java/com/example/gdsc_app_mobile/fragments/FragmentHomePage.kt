package com.example.gdsc_app_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.adapters.EventAdapter
import com.example.gdsc_app_mobile.adapters.FaqAdapter
import com.example.gdsc_app_mobile.models.EventModel
import com.example.gdsc_app_mobile.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentHomePage: Fragment(){

    private var events = ArrayList<EventModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        getEvents()

        return view
    }

    private fun getEvents(){
        val eventCall: Call<List<EventModel>> = ApiClient.getService().getAllEvents()

        eventCall.enqueue(object : Callback<List<EventModel>> {
            override fun onResponse(call: Call<List<EventModel>>,
                response: Response<List<EventModel>>
            ) {
                if(response.isSuccessful){
                    val eventList: List<EventModel>? = response.body()
                    if(eventList != null)
                        for(event in eventList)
                            events.add(event)

//                    val adapter = EventAdapter(requireActivity(), events)
//                    val listView: ListView = view!!.findViewById(R.id.faq_list_view)
//                    listView.adapter = adapter
//                    listView.isClickable = true
//
//                    listView.setOnItemClickListener { parent, view, position, id ->
//                        Toast.makeText(requireContext(), "Position $position", Toast.LENGTH_SHORT).show()
//                    }
                }
                else
                    Toast.makeText(requireContext(), "Unsuccessful response", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

}