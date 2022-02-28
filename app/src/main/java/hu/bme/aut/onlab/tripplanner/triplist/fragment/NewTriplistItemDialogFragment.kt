package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.*
import java.util.*

class NewTriplistItemDialogFragment() : DialogFragment() {
    interface NewTriplistItemDialogListener {
        fun onTriplistItemCreated(newItem: TriplistItem)
        fun onTriplistItemEdited(item: TriplistItem)
    }

    private lateinit var listener: NewTriplistItemDialogListener
    private lateinit var binding: FragmentNewTriplistItemDialogBinding

    /*private lateinit var placeEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var dateDatePicker: DatePicker
    private lateinit var categorySpinner: Spinner
    private lateinit var alreadyVisitedCheckBox: CheckBox*/

    private var item: TriplistItem? = null
    private var type: CreateOrEdit = CreateOrEdit.CREATE

    enum class CreateOrEdit {
        CREATE, EDIT
    }

    constructor(item: TriplistItem) : this() {
        this.item = item
        this.type = CreateOrEdit.EDIT
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewTriplistItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewTriplistItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var title: Int = when (type) {
            CreateOrEdit.CREATE -> R.string.new_triplist_item
            CreateOrEdit.EDIT -> R.string.edit_triplist_item
        }
        binding = FragmentNewTriplistItemDialogBinding.inflate(LayoutInflater.from(context))
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    if (type == CreateOrEdit.CREATE)
                        listener.onTriplistItemCreated(getTriplistItem())
                    else if (type == CreateOrEdit.EDIT)
                        listener.onTriplistItemEdited(getTriplistItem());
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etPlace.text.isNotEmpty()

    /*private fun getContentView(): View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.fragment_new_triplist_item_dialog, null)
        placeEditText = contentView.findViewById(R.id.etPlace)
        countryEditText = contentView.findViewById(R.id.etCountry)
        dateDatePicker = contentView.findViewById(R.id.etDate)
        categorySpinner = contentView.findViewById(R.id.spCategory)
        categorySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )
        alreadyVisitedCheckBox = contentView.findViewById(R.id.cbAlreadyVisited)
        if (item != null) {
            setFields()
        }
        return contentView
    }

    fun setFields() {
        placeEditText.setText(item!!.place)
        countryEditText.setText(item!!.country)
        dateDatePicker.updateDate(2022, 0,2)
        categorySpinner.setSelection(item!!.category.ordinal)
        alreadyVisitedCheckBox.isChecked = item!!.visited
    }*/

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