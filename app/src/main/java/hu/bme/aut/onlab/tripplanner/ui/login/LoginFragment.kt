package hu.bme.aut.onlab.tripplanner.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import co.zsmb.rainbowcake.navigation.navigator
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : RainbowCakeFragment<LoginViewState, LoginViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        requireActivity().setActionBar(binding.toolbar)

        FirebaseApp.initializeApp(requireContext())

        binding.btnRegister.setOnClickListener { viewModel.register(requireContext(), binding.etEmail.text, binding.etPassword.text) }
        binding.btnLogin.setOnClickListener { viewModel.login(navigator, requireContext(), binding.etEmail.text, binding.etPassword.text) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setLogin()
    }

    override fun render(viewState: LoginViewState) {
        when(viewState) {
            is Loading -> {}
            is LoginContent -> {}
        }.exhaustive
    }
}