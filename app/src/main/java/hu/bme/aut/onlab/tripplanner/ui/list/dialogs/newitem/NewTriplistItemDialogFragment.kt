package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import co.zsmb.rainbowcake.base.RainbowCakeDialogFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentNewTriplistItemDialogBinding
import java.util.*

@AndroidEntryPoint
class NewTripListItemDialogFragment() : RainbowCakeDialogFragment<NewTripListItemViewState, NewTripListItemViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    interface NewTripListItemDialogListener {
        fun onTripListItemCreated(newItem: TripListItem)
    }

    interface EditTripListItemDialogListener {
        fun onTripListItemEdited(editedItem: TripListItem)
    }

    private lateinit var newListener: NewTripListItemDialogListener
    private lateinit var editListener: EditTripListItemDialogListener
    private lateinit var binding: FragmentNewTriplistItemDialogBinding

    private lateinit var placeEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateDatePicker: DatePicker
    private lateinit var categorySpinner: Spinner
    private lateinit var alreadyVisitedCheckBox: CheckBox

    private var item: TripListItem? = null
    private var type: CreateOrEdit = CreateOrEdit.CREATE

    enum class CreateOrEdit {
        CREATE, EDIT
    }

    constructor(item: TripListItem) : this() {
        this.item = item
        this.type = CreateOrEdit.EDIT
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setNewTripListItem()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is NewTripListItemDialogListener)
            this.newListener = parent
        else if(parent is EditTripListItemDialogListener)
            this.editListener = parent
        else
            editListener = context as? EditTripListItemDialogListener
                ?: throw RuntimeException("Activity must implement the listener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title: Int = when (type) {
            CreateOrEdit.CREATE -> R.string.new_triplist_item
            CreateOrEdit.EDIT -> R.string.edit_triplist_item
        }

        binding = FragmentNewTriplistItemDialogBinding.inflate(layoutInflater)
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
                        newListener.onTripListItemCreated(getTripListItem())
                    else
                        Toast.makeText(requireActivity().applicationContext, "The place field must be filled", Toast.LENGTH_SHORT).show()
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
                        editListener.onTripListItemEdited(item!!)
                    }
                    else
                        Toast.makeText(requireActivity().applicationContext, "The place field must be filled", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(R.string.button_cancel, null)
                .create()
        }
    }

    private fun isValidCreate() = binding.etPlace.text.isNotEmpty()
    private fun isValidEdit() = placeEditText.text.isNotEmpty()

    @SuppressLint("InflateParams")
    private fun getContentView(): View {
        val contentView = layoutInflater.inflate(R.layout.fragment_new_triplist_item_dialog, null)
        placeEditText = contentView.findViewById(R.id.etPlace)
        countryEditText = contentView.findViewById(R.id.etCountry)
        descriptionEditText = contentView.findViewById(R.id.etDescription)
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
        descriptionEditText.setText(item!!.description)
        dateDatePicker.updateDate(year, month, day)
        categorySpinner.setSelection(item!!.category.ordinal)
        alreadyVisitedCheckBox.isChecked = item!!.visited
    }

    private fun getTripListItem() = TripListItem(
        place = binding.etPlace.text.toString(),
        country = binding.etCountry.text.toString(),
        description = binding.etDescription.text.toString(),
        date = String.format(
            Locale.getDefault(), "%04d.%02d.%02d.",
            binding.etDate.year, binding.etDate.month + 1, binding.etDate.dayOfMonth
        ),
        category = TripListItem.Category.getByOrdinal(binding.spCategory.selectedItemPosition) ?: TripListItem.Category.SIGHTSEEING,
        visited = binding.cbAlreadyVisited.isChecked
    )

    private fun setEditedItem() {
        item?.place = placeEditText.text.toString()
        item?.country = countryEditText.text.toString()
        item?.description = descriptionEditText.text.toString()
        item?.date = String.format(
            Locale.getDefault(), "%04d.%02d.%02d.",
            dateDatePicker.year, dateDatePicker.month + 1, dateDatePicker.dayOfMonth
        )
        item?.category = TripListItem.Category.getByOrdinal(categorySpinner.selectedItemPosition)
            ?: TripListItem.Category.SIGHTSEEING
        item?.visited = alreadyVisitedCheckBox.isChecked
    }

    companion object {
        const val TAG = "NewTriplistItemDialogFragment"
    }

    override fun render(viewState: NewTripListItemViewState) {
        when(viewState) {
            is Loading -> {}
            is NewTripListItemContent -> {}
        }.exhaustive
    }
}