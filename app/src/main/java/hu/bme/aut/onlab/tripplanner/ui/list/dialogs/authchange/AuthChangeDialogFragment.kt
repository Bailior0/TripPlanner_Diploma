package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.authchange

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import co.zsmb.rainbowcake.base.RainbowCakeDialogFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.views.AuthChange
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class AuthChangeDialogFragment : RainbowCakeDialogFragment<AuthChangeViewState, AuthChangeViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    interface AuthChangeDialogListener {
        fun onEmailChanged(password: String?, newEmail: String?)
    }

    private lateinit var listener: AuthChangeDialogListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setAuthChange()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is AuthChangeDialogListener)
            this.listener = parent
        else
            listener = context as? AuthChangeDialogListener
                ?: throw RuntimeException("Activity must implement the AuthChangeDialogListener interface!")
    }

    companion object {
        const val TAG = "AuthChangeDialogFragment"
    }

    override fun render(viewState: AuthChangeViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        is Loading -> FullScreenLoading()
                        is AuthChangeContent -> AuthChange(
                            onOkClick = ::onEmailChange,
                            onCancelClick = ::onCancel
                        )
                    }.exhaustive
                }
            }
        }
    }

    private fun onEmailChange(pass: String, newEmail: String) {
        listener.onEmailChanged(pass, newEmail)
        dialog?.dismiss()
    }

    private fun onCancel() {
        dialog?.dismiss()
    }
}