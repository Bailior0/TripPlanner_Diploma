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

    private lateinit var placeEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var dateDatePicker: DatePicker
    private lateinit var categorySpinner: Spinner
    private lateinit var alreadyVisitedCheckBox: CheckBox

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
        val title: Int = when (type) {
            CreateOrEdit.CREATE -> R.string.new_triplist_item
            CreateOrEdit.EDIT -> R.string.edit_triplist_item
        }

        binding = FragmentNewTriplistItemDialogBinding.inflate(LayoutInflater.from(context))
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        if(item == null) {
            return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(binding.root)
                .setPositiveButton(R.string.button_ok) { _, _ ->
                    if (isValidCreate())
                        listener.onTriplistItemCreated(getTriplistItem())
                }
                .setNegativeButton(R.string.button_cancel, null)
                .create()
        }

        else {
            return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(getContentView())
                .setPositiveButton(R.string.button_ok) { _, _ ->
                    if (isValidEdit()){
                        setEditedItem()
                        listener.onTriplistItemEdited(item!!)
                    }
                }
                .setNegativeButton(R.string.button_cancel, null)
                .create()
        }
    }

    private fun isValidCreate() = binding.etPlace.text.isNotEmpty()
    private fun isValidEdit() = placeEditText.text.isNotEmpty()

    private fun getContentView(): View {
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

    private fun setFields() {
        val day = item!!.date.substring(8, 10).toInt()
        val month = item!!.date.substring(5, 7).toInt()-1
        val year = item!!.date.substring(0, 4).toInt()

        placeEditText.setText(item!!.place)
        countryEditText.setText(item!!.country)
        dateDatePicker.updateDate(year, month, day)
        categorySpinner.setSelection(item!!.category.ordinal)
        alreadyVisitedCheckBox.isChecked = item!!.visited
    }

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

    private fun setEditedItem() {
        item!!.place = placeEditText.text.toString()
        item!!.country = countryEditText.text.toString()
        item!!.date = String.format(
            Locale.getDefault(), "%04d.%02d.%02d.",
            dateDatePicker.year, dateDatePicker.month + 1, dateDatePicker.dayOfMonth
        )
        item!!.category = TriplistItem.Category.getByOrdinal(categorySpinner.selectedItemPosition)
            ?: TriplistItem.Category.SIGHTSEEING
        item!!.visited = alreadyVisitedCheckBox.isChecked
    }

    companion object {
        const val TAG = "NewTriplistItemDialogFragment"
    }
}