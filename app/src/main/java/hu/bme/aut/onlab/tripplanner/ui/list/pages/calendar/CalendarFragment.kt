package hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentCalendarBinding
import java.util.*

@AndroidEntryPoint
class CalendarFragment : RainbowCakeFragment<CalendarViewState, CalendarViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.load()
    }

    override fun render(viewState: CalendarViewState) {
        when(viewState) {
            is Loading -> {}
            is TripsContent -> { setDates(viewState.trips) }
        }.exhaustive
    }

    fun changeCalendar() {
        if(isAdded)
            viewModel.load()
    }

    private fun setDates(items: List<TripListItem>) {
        if(items.isNotEmpty() && isAdded) {
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
                    TripListItem.Category.OUTDOORS -> events.add(EventDay(calendar, R.drawable.outdoors))
                    TripListItem.Category.BEACHES -> events.add(EventDay(calendar, R.drawable.beaches))
                    TripListItem.Category.SIGHTSEEING -> events.add(EventDay(calendar, R.drawable.sightseeing))
                    TripListItem.Category.SKIING -> events.add(EventDay(calendar, R.drawable.skiing))
                    TripListItem.Category.BUSINESS -> events.add(EventDay(calendar, R.drawable.business))
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
}
