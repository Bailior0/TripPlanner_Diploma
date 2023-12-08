package hu.bme.aut.onlab.tripplanner.ui.list.pages.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import co.zsmb.rainbowcake.navigation.navigator
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.ui.list.dialogs.authchange.AuthChangeDialogFragment
import hu.bme.aut.onlab.tripplanner.views.Account
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class AccountFragment: RainbowCakeFragment<AccountViewState, AccountViewModel>(), AuthChangeDialogFragment.AuthChangeDialogListener {
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

    override fun render(viewState: AccountViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        is Loading -> FullScreenLoading()
                        is AccountContent -> Account(
                            viewState.user,
                            onEmailChange = ::onEmailChange,
                            onPasswordChange = ::onPasswordChange,
                            onLogout = ::onLogout
                        )
                    }.exhaustive
                }
            }
        }
    }

    private fun onEmailChange() {
        if(isConnected(requireContext()))
            AuthChangeDialogFragment().show(
                childFragmentManager,
                AuthChangeDialogFragment.TAG
            )
    }

    override fun onEmailChanged(password: String?, newEmail: String?) {
        viewModel.changeEmail(requireContext(), password, newEmail)
    }

    private fun onPasswordChange() {
        if(isConnected(requireContext())) {
            viewModel.changePassword()
            Toast.makeText(context, "Verification email has been sent about your password change", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLogout() {
        viewModel.signOut()
        navigator?.pop()
    }
}