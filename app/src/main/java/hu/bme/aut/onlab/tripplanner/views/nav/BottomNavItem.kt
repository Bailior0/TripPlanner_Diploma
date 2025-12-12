package hu.bme.aut.onlab.tripplanner.views.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title:String, var icon: ImageVector, var screen_route:String){

    data object Trips : BottomNavItem("Trips", Icons.Filled.Menu,"trips")
    data object Calendar: BottomNavItem("Calendar", Icons.Filled.Event,"calendar")
    data object Maps: BottomNavItem("Map",Icons.Filled.Map,"map")
    data object Identifier: BottomNavItem("Identify",Icons.Filled.TravelExplore,"identifier")
    data object Recommendation: BottomNavItem("Explore",Icons.Filled.Explore,"explore")
    data object Account: BottomNavItem("Account",Icons.Filled.AccountCircle,"account")
}