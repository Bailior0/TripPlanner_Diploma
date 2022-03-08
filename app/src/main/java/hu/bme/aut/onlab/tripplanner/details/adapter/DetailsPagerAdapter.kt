package hu.bme.aut.onlab.tripplanner.details.adapter

import android.content.Context
import androidx.fragment.app.*
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.details.fragment.*

class DetailsPagerAdapter(fragmentManager: FragmentManager, private val context: Context) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val informationFragment = InformationFragment()
        val shareFragment = ShareFragment()
        return when (position) {
            0 -> informationFragment
            1 -> shareFragment
            else -> informationFragment
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.info)
            1 -> context.getString(R.string.share)
            else -> ""
        }
    }

    override fun getCount(): Int = 2
}