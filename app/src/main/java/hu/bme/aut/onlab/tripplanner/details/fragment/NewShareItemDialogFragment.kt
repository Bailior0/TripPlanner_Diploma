package hu.bme.aut.onlab.tripplanner.details.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.FragmentNewShareItemDialogBinding

class NewShareItemDialogFragment : DialogFragment() {
    interface NewShareItemDialogListener {
        fun onUploadPost(nick: String, title: String, comment: String)
    }

    private lateinit var listener: NewShareItemDialogListener
    private lateinit var binding: FragmentNewShareItemDialogBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewShareItemDialogListener
            ?: throw RuntimeException("Activity must implement the AuthChangeDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNewShareItemDialogBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.share)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                if(validateForm())
                    listener.onUploadPost(binding.etNick.text.toString(), binding.etTitle.text.toString(), binding.etBody.text.toString())
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun validateForm() = binding.etTitle.text.isNotEmpty() && binding.etBody.text.isNotEmpty() && binding.etNick.text.isNotEmpty()

    companion object {
        const val TAG = "NewShareItemDialogFragment"
    }
}