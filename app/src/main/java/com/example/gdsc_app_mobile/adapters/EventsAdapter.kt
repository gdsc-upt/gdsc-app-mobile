package com.example.gdsc_app_mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.databinding.CalendarCardEventBinding
import com.example.gdsc_app_mobile.models.EventModel
import java.time.format.DateTimeFormatter
import java.util.*

class EventsAdapter(val onClick: (EventModel) -> Unit) :
    RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    val events = mutableListOf<EventModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            CalendarCardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: EventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventsViewHolder(private val binding: CalendarCardEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: EventModel) {
            binding.eventTitle.text = event.title
            binding.eventDescription.text = event.description
            binding.eventTime.text = parseTime(event.start, event.end)
            //TODO("Add the corresponding image to event card")
        }

        private fun parseTime(start : String, end : String) : String{
            val index1 = 11
            val index2 = 15
            val timeStart = start.subSequence(index1, index2)
            val timeEnd = end.subSequence(index1, index2)

            return "From $timeStart to $timeEnd";
        }
    }

}