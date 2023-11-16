package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.onlab.tripplanner.R

@Composable
fun Account(
    userEmail: String,
    onEmailChange: () -> Unit,
    onPasswordChange: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(0.dp, 0.dp, 0.dp, 70.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
                .padding(12.dp, 12.dp, 12.dp, 25.dp)
                .weight(1f, false)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(painter = painterResource(id = R.drawable.icon), contentDescription = null, modifier = Modifier.fillMaxSize())
            }

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                        append("TripPlanner")
                    }
                },
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
            )

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                        append(userEmail)
                    }
                },
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
            )
        }
        Column( ) {
            Button(
                onClick = {
                    onEmailChange()
                },
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10),
            ) {
                Text("Change email")
            }
            Button(
                onClick = {
                    onPasswordChange()
                },
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10),
            ) {
                Text("Change password")
            }
            Button(
                onClick = {
                    onLogout()
                },
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10),
            ) {
                Text("Log out")
            }
        }
    }
}