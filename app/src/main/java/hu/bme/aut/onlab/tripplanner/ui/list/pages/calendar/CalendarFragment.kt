package hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme
import hu.bme.aut.onlab.tripplanner.views.Calendar
import java.util.*

@AndroidEntryPoint
class CalendarFragment : RainbowCakeFragment<CalendarViewState, CalendarViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    // lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel.load()
        viewModel.addListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun render(viewState: CalendarViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                when (viewState) {
                    is Loading -> FullScreenLoading()
                    is CalendarContent -> Calendar(
                        viewState.trips
                    )
                }.exhaustive
            }
        }
    }

    /*fun changeCalendar() {
        if(isAdded)
            viewModel.load()
    }*/

    /*private fun setDates(items: List<TripListItem>) {
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
    }*/
}
