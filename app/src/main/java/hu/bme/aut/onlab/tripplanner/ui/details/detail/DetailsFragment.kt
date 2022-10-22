package hu.bme.aut.onlab.tripplanner.ui.details.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import co.zsmb.rainbowcake.navigation.extensions.applyArgs
import co.zsmb.rainbowcake.navigation.navigator
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentDetailsBinding
import hu.bme.aut.onlab.tripplanner.ui.details.pages.information.InformationFragment
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.ShareFragment

@AndroidEntryPoint
class DetailsFragment : RainbowCakeFragment<DetailsViewState, DetailsViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var trip: TripListItem

    private lateinit var shareFragment: ShareFragment
    private lateinit var informationFragment: InformationFragment

    companion object {
        private const val EXTRA_TRIP = "TRIP"


        fun newInstance(tripsItem: TripListItem): DetailsFragment {
            return DetailsFragment().applyArgs {
                putParcelable(EXTRA_TRIP, tripsItem)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

        informationFragment = InformationFragment()
        shareFragment = ShareFragment()
        binding.mainViewPager.adapter = DetailsPagerAdapter(childFragmentManager, requireContext(), informationFragment, shareFragment)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setActionBar(binding.toolbar)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            navigator?.pop()
        }

        trip = arguments?.getParcelable(EXTRA_TRIP)!!
        viewModel.setTrip(trip)
        shareFragment.setTrip(trip)
        informationFragment.setTrip(trip)
    }

    override fun render(viewState: DetailsViewState) {
        when(viewState) {
            is Loading -> {}
            is DetailsContent -> { requireActivity().actionBar?.title = viewState.trip?.place }
        }.exhaustive
    }
}