package hu.bme.aut.onlab.tripplanner.details.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.FragmentNewShareItemDialogBinding
import hu.bme.aut.onlab.tripplanner.details.data.SharedData

class NewShareItemDialogFragment() : DialogFragment() {
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
        listener = context as? NewShareItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewShareItemDialogListener interface!")
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
                }
                .setNegativeButton(R.string.button_cancel, null)
                .create()
        }
    }

    private fun isValidCreate() = binding.etTitle.text.isNotEmpty() && binding.etBody.text.isNotEmpty() && binding.etNick.text.isNotEmpty()
    private fun isValidEdit() = nickEditText.text.isNotEmpty() && titleEditText.text.isNotEmpty() && commentEditText.text.isNotEmpty()

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
}