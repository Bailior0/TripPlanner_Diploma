package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.base.RainbowCakeDialogFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.views.TripListItem
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

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
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setNewTripListItem()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        this.editListener = parent as EditTripListItemDialogListener
        this.newListener = parent as NewTripListItemDialogListener
        /*if(parent is EditTripListItemDialogListener)
            this.editListener = parent
        else if (parent is NewTripListItemDialogListener)
            this.newListener = parent
        else
            editListener = context as? EditTripListItemDialogListener
                ?: throw RuntimeException("Activity must implement the listener interface!")
   */ }

    companion object {
        const val TAG = "NewTriplistItemDialogFragment"
    }

    override fun render(viewState: NewTripListItemViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        is Loading -> FullScreenLoading()
                        is NewTripListItemContent -> when (type) {
                            CreateOrEdit.CREATE -> TripListItem(
                                null,
                                onOkUploadClick = ::onUpload,
                                onOkEditClick = ::onEdit,
                                onCancelClick = ::onCancel
                            )
                            CreateOrEdit.EDIT -> TripListItem(
                                item,
                                onOkUploadClick = ::onUpload,
                                onOkEditClick = ::onEdit,
                                onCancelClick = ::onCancel
                            )
                        }
                    }.exhaustive
                }
            }
        }
    }

    private fun onUpload(newItem: TripListItem) {
        newListener.onTripListItemCreated(newItem)
        dialog?.dismiss()
    }

    private fun onEdit(editedItem: TripListItem) {
        editListener.onTripListItemEdited(editedItem)
        dialog?.dismiss()
    }

    private fun onCancel() {
        dialog?.dismiss()
    }
}