package hu.bme.aut.onlab.tripplanner.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import hu.bme.aut.onlab.tripplanner.R

object ConnectivityChecker {
    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
        if(actNw != null) {
            if(actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                return true
            if(actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                return true
            if(actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                return true
        }

        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
        return false
    }
}