package hu.bme.aut.onlab.tripplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.onlab.tripplanner.databinding.ActivityMainBinding
import hu.bme.aut.onlab.tripplanner.triplist.TriplistActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            when {
                binding.etUsername.text.toString().isEmpty() -> {
                    binding.etUsername.requestFocus()
                    binding.etUsername.error = "Please enter your username"
                }
                binding.etPassword.text.toString().isEmpty() -> {
                    binding.etPassword.requestFocus()
                    binding.etPassword.error = "Please enter your password"
                }
                else -> {
                    startActivity(Intent(this, TriplistActivity::class.java))
                }
            }
        }
    }
}