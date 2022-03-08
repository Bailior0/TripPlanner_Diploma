package hu.bme.aut.onlab.tripplanner.details

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.onlab.tripplanner.BaseActivity
import hu.bme.aut.onlab.tripplanner.databinding.ActivityDetailsBinding
import hu.bme.aut.onlab.tripplanner.details.adapter.DetailsPagerAdapter
import hu.bme.aut.onlab.tripplanner.details.data.SharedData
import hu.bme.aut.onlab.tripplanner.details.data.WeatherDataHolder
import hu.bme.aut.onlab.tripplanner.details.fragment.*
import hu.bme.aut.onlab.tripplanner.details.model.WeatherData
import hu.bme.aut.onlab.tripplanner.details.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : BaseActivity(), WeatherDataHolder, NewShareItemDialogFragment.NewShareItemDialogListener {

    private lateinit var binding: ActivityDetailsBinding
    var country: String? = null
    var place: String? = null
    var description: String? = null
    var date: String? = null
    var category: String? = null
    var visited: Boolean = false

    private var weatherData: WeatherData? = null

    companion object {
        const val EXTRA_TRIP_COUNTRY = "extra.trip_country"
        const val EXTRA_TRIP_PLACE = "extra.trip_place"
        const val EXTRA_TRIP_DESCRIPTION = "extra.trip_description"
        const val EXTRA_TRIP_DATE = "extra.trip_date"
        const val EXTRA_TRIP_CATEGORY = "extra.trip_category"
        const val EXTRA_TRIP_VISITED = "extra.trip_visited"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        country = intent.getStringExtra(EXTRA_TRIP_COUNTRY)
        place = intent.getStringExtra(EXTRA_TRIP_PLACE)
        description = intent.getStringExtra(EXTRA_TRIP_DESCRIPTION)
        date = intent.getStringExtra(EXTRA_TRIP_DATE)
        category = intent.getStringExtra(EXTRA_TRIP_CATEGORY)
        visited = intent.getBooleanExtra(EXTRA_TRIP_VISITED, false)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = place
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        loadWeatherData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getWeatherData(): WeatherData? {
        return weatherData
    }

    private fun loadWeatherData() {
        NetworkManager.getWeather(place)?.enqueue(object : Callback<WeatherData?> {
            override fun onResponse(
                call: Call<WeatherData?>,
                response: Response<WeatherData?>
            ) {
                if (response.isSuccessful) {
                    weatherData = response.body()
                    binding.mainViewPager.adapter = DetailsPagerAdapter(supportFragmentManager, applicationContext)
                } else {
                    Toast.makeText(this@DetailsActivity, "Couldn't find weather info for this place", Toast.LENGTH_LONG).show()
                    binding.mainViewPager.adapter = DetailsPagerAdapter(supportFragmentManager, applicationContext)
                }
            }

            override fun onFailure(
                call: Call<WeatherData?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Toast.makeText(this@DetailsActivity, "Network request error occured, check LOG", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onUploadPost(nick: String, title: String, comment: String) {
        val newPost = SharedData(uid, userName, nick, title, comment)

        val db = Firebase.firestore

        db.collection(place!!)
            .add(newPost)
            .addOnSuccessListener {
                Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show()}
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show() }
    }
}