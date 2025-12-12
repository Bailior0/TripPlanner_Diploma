package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import hu.bme.aut.onlab.tripplanner.views.theme.BrandPrimary
import hu.bme.aut.onlab.tripplanner.views.theme.OnSurface

@Composable
fun Information(
    trip: TripListItem?,
    weatherList: WeatherData?
) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = trip?.place ?: "Ismeretlen hely",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    color = BrandPrimary
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Public, contentDescription = null, tint = OnSurface)
                    Spacer(Modifier.width(8.dp))
                    Text(text = trip?.country ?: "Ismeretlen ország", color = OnSurface)
                }
                if (!trip?.description.isNullOrBlank()) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = trip!!.description,
                        style = MaterialTheme.typography.body2,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = BrandPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text("Dátum:", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    Text(trip?.date ?: "-")
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Category, contentDescription = null, tint = BrandPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text("Kategória:", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    Text(trip?.category?.name ?: "-")
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text("Státusz:", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (trip?.visited == true) "Már meglátogatva" else "Még nem jártál itt",
                        color = if (trip?.visited == true) Color(0xFF4CAF50) else Color(0xFFB0BEC5)
                    )
                }
            }
        }

        if (weatherList != null && weatherList.weather?.isNotEmpty() == true) {
            val weather = weatherList.weather!![0]
            val bgColor = when (weather.main?.lowercase()) {
                "clear" -> Color(0xFFFFF59D)
                "clouds" -> Color(0xFFB0BEC5)
                "rain" -> Color(0xFF90CAF9)
                else -> Color(0xFFE0E0E0)
            }

            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Cloud, contentDescription = null, tint = BrandPrimary)
                        Spacer(Modifier.width(8.dp))
                        Text("Időjárás", style = MaterialTheme.typography.h6, color = OnSurface)
                    }
                    Spacer(Modifier.height(8.dp))
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${weather.icon}@2x.png",
                        contentDescription = "Weather icon",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "${weather.main ?: ""} – ${weather.description ?: ""}",
                        style = MaterialTheme.typography.body1,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${weatherList.main?.temp ?: "-"} °C",
                        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                        color = BrandPrimary
                    )
                }
            }
        }
    }
}
