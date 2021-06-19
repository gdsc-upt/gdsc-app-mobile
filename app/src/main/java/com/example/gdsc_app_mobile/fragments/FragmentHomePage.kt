package com.example.gdsc_app_mobile.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gdsc_app_mobile.R
import com.example.gdsc_app_mobile.activities.MainActivity
import com.example.gdsc_app_mobile.adapters.EventsAdapter
import com.example.gdsc_app_mobile.databinding.CalendarDayBinding
import com.example.gdsc_app_mobile.databinding.CalendarHeaderBinding
import com.example.gdsc_app_mobile.databinding.FragmentHomePageBinding
import com.example.gdsc_app_mobile.dialogs.DialogFragmentAddEvent
import com.example.gdsc_app_mobile.interfaces.ISelectedEvent
import com.example.gdsc_app_mobile.models.EventModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class FragmentHomePage: Fragment(), ISelectedEvent{

    private val eventsAdapter = EventsAdapter {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.question_to_delete)
            .setPositiveButton(R.string.delete) { _, _ ->
                deleteEvent(it)
            }
            .setNegativeButton(R.string.close, null)
            .show()
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val events = mutableMapOf<LocalDate, List<EventModel>>()

    private var _homePageBinding: FragmentHomePageBinding? = null
    private val homePageBinding get() = _homePageBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homePageBinding = FragmentHomePageBinding.inflate(inflater, container, false)
        (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title).text = getString(
                    R.string.HOME)
        val view = homePageBinding.root

        homePageBinding.calendarRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()

        homePageBinding.homePageCalendar.apply {
            setup(currentMonth.minusMonths(10),
                currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            homePageBinding.homePageCalendar.post {
                // Show today's events initially.
                selectDate(today, homePageBinding)
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val calendarDayBinding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date, homePageBinding)
                    }
                }
            }
        }

        homePageBinding.homePageCalendar.dayBinder = object : DayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.calendarDayBinding.dayText
                val dotView = container.calendarDayBinding.dotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.visibility = View.VISIBLE
                    when (day.date) {
                        today -> {
                            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_turqoise))
                            textView.setBackgroundResource(R.drawable.today_background)
                            dotView.visibility = View.INVISIBLE
                        }
                        selectedDate -> {
                            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
                            textView.setBackgroundResource(R.drawable.today_background)
                            dotView.visibility = View.INVISIBLE
                        }
                        else -> {
                            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_2))
                            textView.background = null
                            dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.visibility = View.INVISIBLE
                    dotView.visibility = View.INVISIBLE
                }
            }

        }

        homePageBinding.homePageCalendar.monthScrollListener = {

            selectDate(it.yearMonth.atDay(1), homePageBinding)

        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }

        homePageBinding.homePageCalendar.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].name.first().toString()
//                        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
        }

        homePageBinding.eventAddEventButton.setOnClickListener{
            addEvent()
        }


        return view
    }

    private fun selectDate(date: LocalDate, binding: FragmentHomePageBinding) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.homePageCalendar.notifyDateChanged(it) }
            binding.homePageCalendar.notifyDateChanged(date)

            //update adapter
            eventsAdapter.apply {
                events.clear()
                events.addAll(this@FragmentHomePage.events[date].orEmpty())
                notifyDataSetChanged()
            }
            binding.calendarDayFrame.text = selectionFormatter.format(date)
        }
    }

    override fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    private fun addEvent() {
        val dialog = DialogFragmentAddEvent()
        // Add a listener to listen to the data from the dialog
        dialog.addListener(this)
        dialog.show(requireActivity().supportFragmentManager, "AddEventDialog")
    }

    override fun deleteEvent(it: EventModel) {
        TODO("Not yet implemented")
    }

    override fun onSelectedEvent(title: String, description: String) {
        TODO("Not yet implemented")
    }

    override fun createPost(title: String, description: String) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        _homePageBinding = null
    }

}