package hu.bme.aut.onlab.tripplanner.views.nav

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.onlab.tripplanner.ui.list.pages.account.AccountFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.IdentifierFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.maps.MapsFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsFragment
import hu.bme.aut.onlab.tripplanner.views.theme.Purple700
import hu.bme.aut.onlab.tripplanner.views.theme.Teal200

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(fragmentManager: FragmentManager, tripsFragment: TripsFragment, calendarFragment: CalendarFragment, mapsFragment: MapsFragment, identifierFragment: IdentifierFragment, accountFragment: AccountFragment) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
        topBar = {
            TopAppBar(
                title = {
                    Text("Trip Planner")
                },
                backgroundColor = Purple700,
                contentColor = Color.White
            )
        },
        bottomBar = { BottomNavigation(navController = navController) },
        /*floatingActionButton = {
            FloatingActionButton(onClick = { onFabClicked() }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }*/
    ) {

        NavigationGraph(navController = navController, fragmentManager = fragmentManager, tripsFragment = tripsFragment, calendarFragment = calendarFragment, mapsFragment = mapsFragment, identifierFragment = identifierFragment, accountFragment = accountFragment)
    }
}
@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Trips,
        BottomNavItem.Calendar,
        BottomNavItem.Maps,
        BottomNavItem.Identifier,
        BottomNavItem.Account
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = Teal200,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, "") },
                label = { Text(text = item.title,
                    fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, fragmentManager: FragmentManager, tripsFragment: TripsFragment, calendarFragment: CalendarFragment, mapsFragment: MapsFragment, identifierFragment: IdentifierFragment, accountFragment: AccountFragment) {
    NavHost(navController, startDestination = BottomNavItem.Trips.screen_route) {
        composable(BottomNavItem.Trips.screen_route) {
            FragmentContainer(
                modifier = Modifier.fillMaxSize(),
                fragmentManager = fragmentManager,
                commit = { add(it, tripsFragment) }
            )
        }
        composable(BottomNavItem.Calendar.screen_route) {
            FragmentContainer(
                modifier = Modifier.fillMaxSize(),
                fragmentManager = fragmentManager,
                commit = { add(it, calendarFragment) }
            )
        }
        composable(BottomNavItem.Maps.screen_route) {
            FragmentContainer(
                modifier = Modifier.fillMaxSize(),
                fragmentManager = fragmentManager,
                commit = { add(it, mapsFragment) }
            )
        }
        composable(BottomNavItem.Identifier.screen_route) {
            FragmentContainer(
                modifier = Modifier.fillMaxSize(),
                fragmentManager = fragmentManager,
                commit = { add(it, identifierFragment) }
            )
        }
        composable(BottomNavItem.Account.screen_route) {
            FragmentContainer(
                modifier = Modifier.fillMaxSize(),
                fragmentManager = fragmentManager,
                commit = { add(it, accountFragment) }
            )
        }
    }
}

@Composable
fun FragmentContainer(
    modifier: Modifier = Modifier,
    fragmentManager: FragmentManager,
    commit: FragmentTransaction.(containerId: Int) -> Unit
) {
    val containerId by rememberSaveable {
        mutableStateOf(View.generateViewId()) }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            fragmentManager.findFragmentById(containerId)?.view
                ?.also { (it.parent as? ViewGroup)?.removeView(it) }
                ?: FragmentContainerView(context)
                    .apply { id = containerId }
                    .also {
                        fragmentManager.commit { commit(it.id) }
                    }
        },
        update = {}
    )
}