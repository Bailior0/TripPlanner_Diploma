package hu.bme.aut.onlab.tripplanner.triplist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.ActivityTriplistBinding

class TriplistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTriplistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriplistBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val triplistPagerAdapter = TriplistPagerAdapter(this)
        binding.mainViewPager.adapter = triplistPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) {
                tab, position -> tab.text = when(position) {
                0 -> getString(R.string.list)
                1 -> getString(R.string.calendar)
                2 -> getString(R.string.map)
                else -> ""
            }
        }.attach()
    }
}