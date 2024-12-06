package hu.bme.aut.onlab.tripplanner.views

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.skydoves.landscapist.glide.GlideImage
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData

@Composable
fun Information(
    trip: TripListItem?,
    weatherList: WeatherData?
) {
    isConnected(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp, 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text(stringResource(R.string.country), Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.country != null)
                Text(trip.country,
                    Modifier
                        .weight(1f)
                        .padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text(stringResource(R.string.place), Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.place != null)
                Text(trip.place,
                    Modifier
                        .weight(1f)
                        .padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text(stringResource(R.string.description), Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.description != null)
                Text(trip.description,
                    Modifier
                        .weight(1f)
                        .padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text(stringResource(R.string.date), Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.date != null)
                Text(trip.date,
                    Modifier
                        .weight(1f)
                        .padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text(stringResource(R.string.category), Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.category != null)
                Text(trip.category.name,
                    Modifier
                        .weight(1f)
                        .padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Visited", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.visited != null) {
                when(trip.visited) {
                    true -> Text(stringResource(R.string.already_visited),
                        Modifier
                            .weight(1f)
                            .padding(start = 50.dp), textAlign = TextAlign.Justify)
                    false -> Text(stringResource(R.string.not_visited),
                        Modifier
                            .weight(1f)
                            .padding(start = 50.dp), textAlign = TextAlign.Justify)
                }
            }
        }
        if(weatherList != null) {
            val weather = weatherList.weather?.first()

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
            ) {
                Text(stringResource(R.string.current_weather), Modifier.weight(1f), textAlign = TextAlign.Justify)
                if(weather?.main != null)
                    Text(weather.main,
                        Modifier
                            .weight(1f)
                            .padding(start = 50.dp), textAlign = TextAlign.Justify)
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
            ) {
                Text(stringResource(R.string.current_weather_description), Modifier.weight(1f), textAlign = TextAlign.Justify)
                if(weather?.description != null)
                    Text(weather.description,
                        Modifier
                            .weight(1f)
                            .padding(start = 50.dp), textAlign = TextAlign.Justify)
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
            ) {
                Text(stringResource(R.string.temperature), Modifier.weight(1f), textAlign = TextAlign.Justify)
                if(weatherList.main?.temp != null)
                    Text(weatherList.main?.temp.toString() + " °C",
                        Modifier
                            .weight(1f)
                            .padding(start = 50.dp), textAlign = TextAlign.Justify)
            }
            if(weather != null) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight(),
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        val url = "https://openweathermap.org/img/w/${weather.icon}.png"
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            onLoading = {
                                Log.i("dologl", it.toString())

                            },
                            onSuccess = {
                                Log.i("dologs", it.toString())

                            },
                            onError = {
                                Log.i("dologe", it.toString())

                            }
                        )
                        //Log.i("dolog", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Parliament_Building%2C_Budapest%2C_outside.jpg/1280px-Parliament_Building%2C_Budapest%2C_outside.jpg")
                        /*GlideImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            imageModel = {"https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/640px-PNG_transparency_demonstration_1.png"}
                        )
                        GlideImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            imageModel = {"https://openweathermap.org/img/wn/10d@2x.png"}
                        )*/

                    }
                }
            }
        }

    }
}