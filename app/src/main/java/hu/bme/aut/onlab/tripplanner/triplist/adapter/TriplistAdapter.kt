package hu.bme.aut.onlab.tripplanner.triplist.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.ItemTriplistListBinding
import hu.bme.aut.onlab.tripplanner.triplist.TriplistActivity

class TriplistAdapter(private val listener: TriplistItemClickListener) : RecyclerView.Adapter<TriplistAdapter.TriplistViewHolder>() {

    private val items = mutableListOf<TriplistItem>()
    private lateinit var activity: TriplistActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriplistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_triplist_list, parent, false)
        return TriplistViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: TriplistViewHolder, position: Int) {
        val triplistItem = items[position]

        holder.binding.cbVisited.setOnCheckedChangeListener { _, isChecked ->
            triplistItem.visited = isChecked
            listener.onItemChanged(triplistItem)
            holder.bind(triplistItem.country, triplistItem.place, triplistItem.description, triplistItem.date, triplistItem.category.name,  triplistItem.visited)
        }

        holder.binding.ivIcon.setImageResource(getImageResource(triplistItem.category))
        holder.binding.tvCountry.text = triplistItem.country
        holder.binding.tvPlace.text = triplistItem.place
        holder.binding.tvDate.text = triplistItem.date
        holder.binding.cbVisited.isChecked = triplistItem.visited
        holder.binding.tvCategory.text = triplistItem.category.name

        holder.binding.ibEdit.setOnClickListener {
            listener.onItemEdited(triplistItem)
        }

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemRemoved(triplistItem)
        }

        val background = holder.binding.root.background as GradientDrawable
        background.setColor(ContextCompat.getColor(activity.applicationContext, getBackgroundColor(triplistItem.category)))

        holder.bind(triplistItem.country, triplistItem.place, triplistItem.description, triplistItem.date, triplistItem.category.name,  triplistItem.visited)
    }

    private fun getBackgroundColor(category: TriplistItem.Category): Int {
        return when (category) {
            TriplistItem.Category.OUTDOORS -> R.color.outdoors
            TriplistItem.Category.BEACHES -> R.color.beaches
            TriplistItem.Category.SIGHTSEEING -> R.color.sightseeing
            TriplistItem.Category.SKIING -> R.color.skiing
            TriplistItem.Category.BUSINESS -> R.color.business
        }
    }

    private fun getImageResource(category: TriplistItem.Category): Int {
        return when (category) {
            TriplistItem.Category.OUTDOORS -> R.drawable.outdoors
            TriplistItem.Category.BEACHES -> R.drawable.beaches
            TriplistItem.Category.SIGHTSEEING -> R.drawable.sightseeing
            TriplistItem.Category.SKIING -> R.drawable.skiing
            TriplistItem.Category.BUSINESS -> R.drawable.business
        }
    }

    fun addItem(item: TriplistItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
        sort()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItem(triplistItems: List<TriplistItem>) {
        items.clear()
        items.addAll(triplistItems)
        notifyDataSetChanged()
        sort()
    }

    fun removeItem(item: TriplistItem) {
        val pos = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(pos)
    }

    fun editItem(item: TriplistItem) {
        val position = items.indexOf(items.find { it.id == item.id })
        items.removeAt(position)
        items.add(position, item)
        notifyItemChanged(position)
        sort()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sort() {
        items.sortBy { it.date }
        notifyDataSetChanged()
    }

    fun setActivity(act: TriplistActivity) {
        activity = act
    }

    override fun getItemCount(): Int = items.size

    interface TriplistItemClickListener {
        fun onItemChanged(item: TriplistItem)
        fun onItemRemoved(item: TriplistItem)
        fun onItemEdited(item: TriplistItem)
        fun onTripSelected(country: String?, place: String?, description: String?, date: String?, category: String?, visited: Boolean)
    }

    inner class TriplistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemTriplistListBinding.bind(itemView)
        var country: String? = null
        var place: String? = null
        var description: String? = null
        var date: String? = null
        var category: String? = null
        var visited: Boolean = false

        init {
            binding.root.setOnClickListener { listener.onTripSelected(country, place, description, date, category, visited) }
        }

        fun bind(newCountry: String?, newPlace: String?, newDescription: String?, newDate: String?, newCategory: String?, newVisited: Boolean) {
            country = newCountry
            binding.tvCountry.text = country
            place = newPlace
            binding.tvPlace.text = place
            description = newDescription
            date = newDate
            binding.tvDate.text = date
            category = newCategory
            binding.tvCategory.text = category
            visited = newVisited
            binding.cbVisited.isChecked = visited
        }
    }
}