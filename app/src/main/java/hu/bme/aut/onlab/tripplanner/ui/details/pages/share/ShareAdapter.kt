package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.databinding.CardShareBinding

class ShareAdapter(private val context: Context, private val listener: ShareListItemClickListener) : ListAdapter<SharedData, ShareAdapter.ShareViewHolder>(ShareComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareViewHolder {
        val binding = CardShareBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShareViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        val shareListItem = getItem(position)

        with(holder.binding) {
            tvNickname.text = shareListItem.nickname
            tvTitle.text = shareListItem.title
            tvBody.text = shareListItem.body

            tvLikeNum.text = shareListItem.liked.size.toString()
            tvUseful.setText(R.string.useful)

            if(shareListItem.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                ibEdit.visibility = View.GONE
                ibRemove.visibility = View.GONE
            }
            else {
                ibEdit.visibility = View.VISIBLE
                ibRemove.visibility = View.VISIBLE
            }

            ibEdit.setOnClickListener {
                if(isConnected(context))
                    listener.onItemEdited(shareListItem)
            }

            ibRemove.setOnClickListener {
                if(isConnected(context))
                    listener.onItemRemoved(shareListItem)
            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val userFound = shareListItem.liked.find { it == userId }

            if(!userFound.isNullOrEmpty()) {
                ibLike.setImageResource(R.drawable.ic_baseline_thumb_up_24_blue)
                tvLikeNum.setTextColor(ContextCompat.getColor(context, R.color.blue))
                tvUseful.setTextColor(ContextCompat.getColor(context, R.color.blue))
            }
            else {
                ibLike.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                tvLikeNum.setTextColor(ContextCompat.getColor(context, R.color.color1))
                tvUseful.setTextColor(ContextCompat.getColor(context, R.color.color1))
            }

            ibLike.setOnClickListener {
                if(isConnected(context))
                    listener.onItemLiked(shareListItem)
            }
        }
    }

    interface ShareListItemClickListener {
        fun onItemRemoved(item: SharedData)
        fun onItemEdited(item: SharedData)
        fun onItemLiked(item: SharedData)
    }

    inner class ShareViewHolder(val binding: CardShareBinding) : RecyclerView.ViewHolder(binding.root)
}

object ShareComparator : DiffUtil.ItemCallback<SharedData>() {

    override fun areItemsTheSame(oldItem: SharedData, newItem: SharedData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SharedData, newItem: SharedData): Boolean {
        return oldItem.liked == newItem.liked
    }
}