package hu.bme.aut.onlab.tripplanner.ui.details.pages.sharedialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import co.zsmb.rainbowcake.base.RainbowCakeDialogFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.databinding.FragmentNewShareItemDialogBinding

@AndroidEntryPoint
class NewShareItemDialogFragment() : RainbowCakeDialogFragment<ShareItemViewState, ShareItemViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    interface NewShareItemDialogListener {
        fun onUploadPost(nick: String, title: String, comment: String)
        fun onEditPost(item: SharedData)
    }

    private lateinit var listener: NewShareItemDialogListener
    private lateinit var binding: FragmentNewShareItemDialogBinding

    private lateinit var nickEditText: EditText
    private lateinit var titleEditText: EditText
    private lateinit var commentEditText: EditText

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setShareItem()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title: Int = when (type) {
            CreateOrEdit.CREATE -> R.string.create_comment
            CreateOrEdit.EDIT -> R.string.edit_comment
        }

        binding = FragmentNewShareItemDialogBinding.inflate(layoutInflater)

        if(item == null) {
            return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(binding.root)
                .setPositiveButton(R.string.button_ok) { _, _ ->
                    if(isValidCreate())
                        listener.onUploadPost(binding.etNick.text.toString(), binding.etTitle.text.toString(), binding.etBody.text.toString())
                    else
                        Toast.makeText(requireActivity().applicationContext, "All fields must be filled", Toast.LENGTH_SHORT).show()
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
                        listener.onEditPost(item!!)
                    }
                    else
                        Toast.makeText(requireActivity().applicationContext, "All fields must be filled", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(R.string.button_cancel, null)
                .create()
        }
    }

    private fun isValidCreate() = binding.etTitle.text.isNotEmpty() && binding.etBody.text.isNotEmpty() && binding.etNick.text.isNotEmpty()
    private fun isValidEdit() = nickEditText.text.isNotEmpty() && titleEditText.text.isNotEmpty() && commentEditText.text.isNotEmpty()

    @SuppressLint("InflateParams")
    private fun getContentView(): View {
        val contentView = layoutInflater.inflate(R.layout.fragment_new_share_item_dialog, null)
        nickEditText = contentView.findViewById(R.id.etNick)
        titleEditText = contentView.findViewById(R.id.etTitle)
        commentEditText = contentView.findViewById(R.id.etBody)
        if (item != null) {
            setFields()
        }
        return contentView
    }

    private fun setFields() {
        nickEditText.setText(item!!.nickname)
        titleEditText.setText(item!!.title)
        commentEditText.setText(item!!.body)
    }

    private fun setEditedItem() {
        item!!.nickname = nickEditText.text.toString()
        item!!.title = titleEditText.text.toString()
        item!!.body = commentEditText.text.toString()
    }

    companion object {
        const val TAG = "NewShareItemDialogFragment"
    }

    override fun render(viewState: ShareItemViewState) {
        when(viewState) {
            is Loading -> {}
            is ShareItemContent -> {}
        }.exhaustive
    }
}