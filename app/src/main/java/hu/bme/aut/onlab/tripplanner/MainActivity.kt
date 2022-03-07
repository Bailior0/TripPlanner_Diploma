package hu.bme.aut.onlab.tripplanner

import android.content.Intent
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import hu.bme.aut.onlab.tripplanner.databinding.ActivityMainBinding
import hu.bme.aut.onlab.tripplanner.triplist.TriplistActivity

class MainActivity : BaseActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        FirebaseApp.initializeApp(this)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener { registerClick() }
        binding.btnLogin.setOnClickListener { loginClick() }
    }

    private fun validateForm(): Boolean {
        if (binding.etEmail.text.isEmpty()) {
            binding.etEmail.requestFocus()
            binding.etEmail.error = "Please enter your email"
            return false
        }
        if (binding.etPassword.text.isEmpty()) {
            binding.etPassword.requestFocus()
            binding.etPassword.error = "Please enter your password"
            return false
        }
        return true
    }

    private fun registerClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnSuccessListener { result ->
                hideProgressDialog()

                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)
                firebaseUser?.sendEmailVerification()

                toast("Registration was successful\nVerification email has been sent")
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                toast(exception.message)
            }
    }

    private fun loginClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnSuccessListener {
                hideProgressDialog()
                if(firebaseAuth.currentUser!!.isEmailVerified) {
                    startActivity(Intent(this@MainActivity, TriplistActivity::class.java))
                    finish()
                }
                else
                    toast("Please verify your email")


            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                toast(exception.localizedMessage)
            }
    }
}