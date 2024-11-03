package hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.views.Identifier
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class IdentifierFragment: RainbowCakeFragment<IdentifierViewState, IdentifierViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.load()
    }

    override fun render(viewState: IdentifierViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        is Loading -> FullScreenLoading()
                        is IdentifierContent -> Identifier(
                            viewState.prediction,
                            viewState.image,
                            onImageChosen = ::onImageChosen,
                            onClicked = ::onClicked
                        )
                    }.exhaustive
                }
            }
        }
    }

    private fun onImageChosen(image: Bitmap, context: Context) {
        viewModel.identify(image, context)
    }

    private fun onClicked() {
        if(isConnected(requireContext())) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://hu.wikipedia.org/wiki/Orsz%C3%A1gh%C3%A1z")
            startActivity(intent)
        }
        /*else
            Snackbar.make(requireView(), R.string.network_error, Snackbar.LENGTH_SHORT).show()*/
    }
}
