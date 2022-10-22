package hu.bme.aut.onlab.tripplanner.ui.details.detail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.ui.details.pages.information.InformationFragment
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.ShareFragment

class DetailsPagerAdapter(fragmentManager: FragmentManager, private val context: Context, private val informationFragment: InformationFragment, private val shareFragment: ShareFragment) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
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