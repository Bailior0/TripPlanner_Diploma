package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.FragmentAuthChangeDialogBinding

class AuthChangeDialogFragment : DialogFragment() {
    interface AuthChangeDialogListener {
        fun onEmailChanged(password: String?, newEmail: String?)
    }

    private lateinit var listener: AuthChangeDialogListener
    private lateinit var binding: FragmentAuthChangeDialogBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
}