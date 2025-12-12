package hu.bme.aut.onlab.tripplanner

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import co.zsmb.rainbowcake.navigation.SimpleNavActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.ui.list.tripslist.TripListFragment
import hu.bme.aut.onlab.tripplanner.ui.login.LoginFragment
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : SimpleNavActivity() {
    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234
    }
    private val locationPermissionGranted = mutableStateOf(false)

    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (savedInstanceState == null) {
            getLocationPermission()
            if (auth.currentUser != null) {
                navigator.add(TripListFragment())
            } else {
                navigator.add(LoginFragment())
            }
        }
    }

    override fun onStart() {
        super.onStart()

        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                navigator.setStack(LoginFragment())
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION

            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted.value = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted.value=true
        }
    }
}