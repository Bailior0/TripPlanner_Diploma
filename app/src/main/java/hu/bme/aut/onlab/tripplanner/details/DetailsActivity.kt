package hu.bme.aut.onlab.tripplanner.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var country: String? = null
    private var place: String? = null
    private var description: String? = null

    companion object {
        private const val TAG = "DetailsActivity"
        const val EXTRA_TRIP_COUNTRY = "extra.trip_country"
        const val EXTRA_TRIP_PLACE = "extra.trip_place"
        const val EXTRA_TRIP_DESCRIPTION = "extra.trip_description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        country = intent.getStringExtra(EXTRA_TRIP_COUNTRY)
        place = intent.getStringExtra(EXTRA_TRIP_PLACE)
        description = intent.getStringExtra(EXTRA_TRIP_DESCRIPTION)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = place
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val text = "Country: $country\n\nPlace: $place\n\nDescription: $description"
        binding.tvDescription.text = text
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}