package hu.bme.aut.onlab.tripplanner.ui.list.pages.recommendation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.views.RecommendationView
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class RecommendationFragment : RainbowCakeFragment<RecommendationViewState, RecommendationViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadRecommendations()
    }

    override fun render(viewState: RecommendationViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                when (viewState) {
                    is RecommendationLoading -> FullScreenLoading()
                    is RecommendationContent -> RecommendationView(
                        items = viewState.recommendations,
                        onSelect = { rec ->
                            // TODO navigate to details or open external link
                        },
                        onFeedback = { rec, liked ->
                            viewModel.sendFeedback(rec, liked)
                        }
                    )
                }
            }
        }
    }
}
