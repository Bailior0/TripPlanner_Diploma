package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.FragmentMapsBinding

@AndroidEntryPoint
class MapsFragment : RainbowCakeFragment<MapsViewState, MapsViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentMapsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setMap(requireContext())
    }

    override fun render(viewState: MapsViewState) {
        when(viewState) {
            is Loading -> {}
            is TripsContent -> {
                if(viewState.maps != null)
                    setMap(viewState.maps)
                else setMap{ googleMap -> googleMap.clear() }
            }
        }.exhaustive
    }

    fun changeMap() {
        if(isAdded)
            viewModel.setMap(requireContext())
    }

    private fun setMap(callback: OnMapReadyCallback) {
        if(isAdded) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }
}
