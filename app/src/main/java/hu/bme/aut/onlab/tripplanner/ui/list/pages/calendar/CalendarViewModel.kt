package hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val calendarPresenter: CalendarPresenter) : RainbowCakeViewModel<CalendarViewState>(
    Loading
) {

    fun load() = execute {
        viewState = TripsContent(loading = true)
        viewState = TripsContent(trips = calendarPresenter.load(), loading = false)
    }
}