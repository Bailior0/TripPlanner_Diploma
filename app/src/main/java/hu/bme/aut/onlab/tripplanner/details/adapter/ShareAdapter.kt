package hu.bme.aut.onlab.tripplanner.details.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.CardShareBinding
import hu.bme.aut.onlab.tripplanner.details.DetailsActivity
import hu.bme.aut.onlab.tripplanner.details.data.SharedData

class ShareAdapter(private val context: Context, private val listener: SharelistItemClickListener) : RecyclerView.Adapter<ShareAdapter.ShareViewHolder>() {

    private var postList = mutableListOf<SharedData>()
    private var lastPosition = -1
    private lateinit var activity: DetailsActivity

    inner class ShareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = CardShareBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareAdapter.ShareViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_share, parent, false)

        return ShareViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        val tmpPost = postList[position]

        holder.binding.tvNickname.text = tmpPost.nickname
        holder.binding.tvTitle.text = tmpPost.title
        holder.binding.tvBody.text = tmpPost.body

        holder.binding.tvLikeNum.text = tmpPost.liked.size.toString()
        holder.binding.tvUseful.setText(R.string.useful)

        setAnimation(holder.itemView, position)

        if(tmpPost.uid != activity.getUId()) {
            holder.binding.ibEdit.visibility = View.GONE
            holder.binding.ibRemove.visibility = View.GONE
        }
        else {
            holder.binding.ibEdit.visibility = View.VISIBLE
            holder.binding.ibRemove.visibility = View.VISIBLE
        }

        holder.binding.ibEdit.setOnClickListener {
            if(activity.isOnline(context))
                listener.onItemEdited(tmpPost)
        }

        holder.binding.ibRemove.setOnClickListener {
            if(activity.isOnline(context))
                listener.onItemRemoved(tmpPost)
        }

        val userId = activity.getUId()
        val userFound = tmpPost.liked.find { it == userId }

        if(!userFound.isNullOrEmpty()) {
            holder.binding.ibLike.setImageResource(R.drawable.ic_baseline_thumb_up_24_blue)
            holder.binding.tvLikeNum.setTextColor(activity.resources.getColor(R.color.blue))
            holder.binding.tvLikeNum.setTypeface(null, Typeface.BOLD)
            holder.binding.tvUseful.setTextColor(activity.resources.getColor(R.color.blue))
            holder.binding.tvUseful.setTypeface(null, Typeface.BOLD)
        }
        else {
            holder.binding.ibLike.setImageResource(R.drawable.ic_baseline_thumb_up_24)
            holder.binding.tvLikeNum.setTextColor(activity.resources.getColor(R.color.color1))
            holder.binding.tvUseful.setTextColor(activity.resources.getColor(R.color.color1))
        }

        holder.binding.ibLike.setOnClickListener {
            if(activity.isOnline(context))
                listener.onItemLiked(tmpPost)
        }
    }

    fun addPost(post: SharedData, id: String) {
        post.id = id
        postList.add(post)
        notifyItemInserted(postList.lastIndex)
    }

    fun editPost(post: SharedData, id: String) {
        val position = postList.indexOf(postList.find { it.id == id })
        postList.removeAt(position)
        postList.add(position, post)
        notifyItemChanged(position)
    }

    fun removePost(id: String) {
        val position = postList.indexOf(postList.find { it.id == id })
        postList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setActivity(act: DetailsActivity) {
        activity = act
    }

    interface SharelistItemClickListener {
        fun onItemRemoved(item: SharedData)
        fun onItemEdited(item: SharedData)
        fun onItemLiked(item: SharedData)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int = postList.size
}