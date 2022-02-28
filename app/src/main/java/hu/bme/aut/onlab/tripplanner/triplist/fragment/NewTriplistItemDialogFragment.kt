package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.*
import java.util.*

class NewTriplistItemDialogFragment : DialogFragment() {
    interface NewTriplistItemDialogListener {
        fun onTriplistItemCreated(newItem: TriplistItem)
    }

    private lateinit var listener: NewTriplistItemDialogListener

    private lateinit var binding: FragmentNewTriplistItemDialogBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewTriplistItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewTriplistItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNewTriplistItemDialogBinding.inflate(LayoutInflater.from(context))
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_triplist_item)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onTriplistItemCreated(getTriplistItem())
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etPlace.text.isNotEmpty()

    private fun getTriplistItem() = TriplistItem(
        place = binding.etPlace.text.toString(),
        country = binding.etCountry.text.toString(),
        date = String.format(
            Locale.getDefault(), "%04d.%02d.%02d.",
            binding.etDate.year, binding.etDate.month + 1, binding.etDate.dayOfMonth
        ),
        category = TriplistItem.Category.getByOrdinal(binding.spCategory.selectedItemPosition) ?: TriplistItem.Category.SIGHTSEEING,
        visited = binding.cbAlreadyVisited.isChecked
    )

    companion object {
        const val TAG = "NewTriplistItemDialogFragment"
    }
}