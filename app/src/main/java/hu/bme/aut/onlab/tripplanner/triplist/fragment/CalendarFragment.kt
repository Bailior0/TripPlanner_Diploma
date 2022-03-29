package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.TriplistDatabase
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentCalendarBinding
import java.util.*
import kotlin.concurrent.thread

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var database: TriplistDatabase
    private lateinit var items: List<TriplistItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    fun setData() {
        if(isAdded) {
            database = TriplistDatabase.getDatabase(requireActivity().applicationContext)
            items = emptyList()
            thread {
                items = database.triplistItemDao().getAll()
                database.close()
                requireActivity().runOnUiThread {
                    if(items.isNotEmpty())
                        setDates(items)
                }
            }
        }
    }

    private fun setDates(items: List<TriplistItem>) {
        val calendars = mutableListOf<Calendar>()
        val events = mutableListOf<EventDay>()
        for (item in items) {
            val day = item.date.substring(8, 10).toInt()
            val month = item.date.substring(5, 7).toInt()-1
            val year = item.date.substring(0, 4).toInt()

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            calendars.add(calendar)

            when (item.category) {
                TriplistItem.Category.OUTDOORS -> events.add(EventDay(calendar, R.drawable.outdoors))
                TriplistItem.Category.BEACHES -> events.add(EventDay(calendar, R.drawable.beaches))
                TriplistItem.Category.SIGHTSEEING -> events.add(EventDay(calendar, R.drawable.sightseeing))
                TriplistItem.Category.SKIING -> events.add(EventDay(calendar, R.drawable.skiing))
                TriplistItem.Category.BUSINESS -> events.add(EventDay(calendar, R.drawable.business))
            }
        }

        binding.calendarView.setEvents(events)
        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                for ((index, item) in items.withIndex()) {
                    if(eventDay == EventDay(calendars[index]))
                        Toast.makeText(requireActivity().applicationContext, item.place, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}