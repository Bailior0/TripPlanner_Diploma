package hu.bme.aut.onlab.tripplanner.details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.onlab.tripplanner.databinding.CardShareBinding
import hu.bme.aut.onlab.tripplanner.details.data.SharedData

class ShareAdapter(private val context: Context) : ListAdapter<SharedData, ShareAdapter.ShareViewHolder>(ItemCallback) {

    private var postList: MutableList<SharedData> = ArrayList()
    private var lastPosition = -1

    class ShareViewHolder(binding: CardShareBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvNickname: TextView = binding.tvNickname
        val tvTitle: TextView = binding.tvTitle
        val tvBody: TextView = binding.tvBody
        val imgPost: ImageView = binding.imgPost
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ShareViewHolder(CardShareBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        val tmpPost = postList[position]
        holder.tvNickname.text = tmpPost.nickname
        holder.tvTitle.text = tmpPost.title
        holder.tvBody.text = tmpPost.body

        if (tmpPost.imageUrl.isNullOrBlank()) {
            holder.imgPost.visibility = View.GONE
        } else {
            Glide.with(context).load(tmpPost.imageUrl).into(holder.imgPost)
            holder.imgPost.visibility = View.VISIBLE
        }

        setAnimation(holder.itemView, position)
    }

    fun addPost(post: SharedData?) {
        post ?: return

        postList.add(post)
        submitList(postList)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    companion object {
        object ItemCallback : DiffUtil.ItemCallback<SharedData>() {
            override fun areItemsTheSame(oldItem: SharedData, newItem: SharedData): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: SharedData, newItem: SharedData): Boolean {
                return oldItem == newItem
            }
        }
    }
}