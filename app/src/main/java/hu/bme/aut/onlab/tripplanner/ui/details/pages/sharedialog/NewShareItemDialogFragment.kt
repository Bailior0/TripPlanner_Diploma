package hu.bme.aut.onlab.tripplanner.ui.details.pages.sharedialog

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import co.zsmb.rainbowcake.base.RainbowCakeDialogFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.views.ShareItem
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class NewShareItemDialogFragment() : RainbowCakeDialogFragment<ShareItemViewState, ShareItemViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    interface NewShareItemDialogListener {
        fun onUploadPost(nick: String, title: String, comment: String, image: Bitmap?)
        fun onEditPost(item: SharedData, image: Bitmap?)
    }

    private lateinit var listener: NewShareItemDialogListener

    private var item: SharedData? = null
    private var type: CreateOrEdit = CreateOrEdit.CREATE

    enum class CreateOrEdit {
        CREATE, EDIT
    }

    constructor(item: SharedData) : this() {
        this.item = item
        this.type = CreateOrEdit.EDIT
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is NewShareItemDialogListener)
            this.listener = parent
        else
            listener = context as? NewShareItemDialogListener
                ?: throw RuntimeException("Activity must implement the NewShareItemDialogListener interface!")
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

        viewModel.setShareItem()
    }

    companion object {
        const val TAG = "NewShareItemDialogFragment"
    }

    override fun render(viewState: ShareItemViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        is Loading -> FullScreenLoading()
                        is ShareItemContent -> when (type) {
                            CreateOrEdit.CREATE -> ShareItem(
                                null,
                                onOkUploadClick = ::onUpload,
                                onOkEditClick = ::onEdit,
                                onCancelClick = ::onCancel
                            )
                            CreateOrEdit.EDIT -> ShareItem(
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

    private fun onUpload(nick: String, title: String, comment: String, image: Bitmap?) {
        listener.onUploadPost(nick, title, comment, image)
        dialog?.dismiss()
    }

    private fun onEdit(newItem: SharedData, image: Bitmap?) {
        listener.onEditPost(newItem, image)
        dialog?.dismiss()
    }

    private fun onCancel() {
        dialog?.dismiss()
    }
}