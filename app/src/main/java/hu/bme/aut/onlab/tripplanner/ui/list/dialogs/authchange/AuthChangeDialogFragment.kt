package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.authchange

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.rainbowcake.base.RainbowCakeDialogFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.FragmentAuthChangeDialogBinding

@AndroidEntryPoint
class AuthChangeDialogFragment : RainbowCakeDialogFragment<AuthChangeViewState, AuthChangeViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    interface AuthChangeDialogListener {
        fun onEmailChanged(password: String?, newEmail: String?)
    }

    private lateinit var listener: AuthChangeDialogListener
    private lateinit var binding: FragmentAuthChangeDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentAuthChangeDialogBinding.inflate(layoutInflater)

        binding.etSecondTextInputLayout.hint = "New email"
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.change_email)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                listener.onEmailChanged(binding.etFirstText.text.toString(), binding.etSecondText.text.toString())
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    companion object {
        const val TAG = "AuthChangeDialogFragment"
    }

    override fun render(viewState: AuthChangeViewState) {
        when(viewState) {
            is Loading -> {}
            is AuthChangeContent -> {}
        }.exhaustive
    }
}