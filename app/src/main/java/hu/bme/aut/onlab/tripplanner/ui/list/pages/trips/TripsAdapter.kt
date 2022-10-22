package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.databinding.ItemTriplistListBinding

class TripListAdapter(private val listener: TripListItemClickListener) : ListAdapter<TripListItem, TripListAdapter.TripListViewHolder>(TripComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListViewHolder {
        val binding = ItemTriplistListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TripListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripListViewHolder, position: Int) {
        val tripListItem = getItem(position)

        with(holder.binding) {
            cbVisited.setOnCheckedChangeListener { _, isChecked ->
                tripListItem.visited = isChecked
                listener.onItemChanged(tripListItem)
            }

            ivIcon.setImageResource(getImageResource(tripListItem.category))
            tvCountry.text = tripListItem.country
            tvPlace.text = tripListItem.place
            tvDate.text = tripListItem.date
            cbVisited.isChecked = tripListItem.visited
            tvCategory.text = tripListItem.category.name

            ibEdit.setOnClickListener {
                listener.onItemEdited(tripListItem)
            }

            ibRemove.setOnClickListener {
                listener.onItemRemoved(tripListItem)
            }

            root.setOnClickListener {
                listener.onTripSelected(tripListItem)
            }

            val background = root.background as GradientDrawable
            background.setColor(ContextCompat.getColor(holder.itemView.context, getBackgroundColor(tripListItem.category)))
        }
    }

    private fun getBackgroundColor(category: TripListItem.Category): Int {
        return when (category) {
            TripListItem.Category.OUTDOORS -> R.color.outdoors
            TripListItem.Category.BEACHES -> R.color.beaches
            TripListItem.Category.SIGHTSEEING -> R.color.sightseeing
            TripListItem.Category.SKIING -> R.color.skiing
            TripListItem.Category.BUSINESS -> R.color.business
        }
    }

    private fun getImageResource(category: TripListItem.Category): Int {
        return when (category) {
            TripListItem.Category.OUTDOORS -> R.drawable.outdoors
            TripListItem.Category.BEACHES -> R.drawable.beaches
            TripListItem.Category.SIGHTSEEING -> R.drawable.sightseeing
            TripListItem.Category.SKIING -> R.drawable.skiing
            TripListItem.Category.BUSINESS -> R.drawable.business
        }
    }

    interface TripListItemClickListener {
        fun onItemChanged(item: TripListItem)
        fun onItemRemoved(item: TripListItem)
        fun onItemEdited(item: TripListItem)
        fun onTripSelected(tripListItem: TripListItem)
    }

    inner class TripListViewHolder(val binding: ItemTriplistListBinding) : RecyclerView.ViewHolder(binding.root)
}

object TripComparator : DiffUtil.ItemCallback<TripListItem>() {

    override fun areItemsTheSame(oldItem: TripListItem, newItem: TripListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TripListItem, newItem: TripListItem): Boolean {
        return oldItem == newItem
    }
}