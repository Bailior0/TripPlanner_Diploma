package hu.bme.aut.onlab.tripplanner

import android.os.Bundle
import co.zsmb.rainbowcake.navigation.SimpleNavActivity
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.ui.list.tripslist.TripListFragment
import hu.bme.aut.onlab.tripplanner.ui.login.LoginFragment

@AndroidEntryPoint
class MainActivity : SimpleNavActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            //navigator.add(LoginFragment())
            navigator.add(TripListFragment())
        }
    }
}