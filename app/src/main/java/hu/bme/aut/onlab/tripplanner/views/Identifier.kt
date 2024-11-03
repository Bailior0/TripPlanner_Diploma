package hu.bme.aut.onlab.tripplanner.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.material.snackbar.Snackbar
import com.skydoves.landscapist.glide.GlideImage
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

@Composable
fun Identifier(
    prediction: String,
    image: Bitmap?,
    onImageChosen: (Bitmap, Context) -> Unit,
    onClicked: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if(uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
            bitmap?.let {  btm ->
                onImageChosen(btm, context)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 70.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .size(400.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { launcher.launch("image/*") }
        ) {
            if (image != null) {
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            } else {
                Icon(
                    imageVector  = Icons.Outlined.PhotoLibrary,
                    null,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            }
        }
        //Text(text = prediction)

        if(prediction != "")
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                //.clickable(onClick = { onClicked() })
                .height(IntrinsicSize.Min)
                .padding(all = 16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight(),
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Transparent,
                            shape = CircleShape,
                        )
                        .requiredSize(48.dp),
                ) {
                    //if (imageUrl != null) {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            imageModel = {"https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Parliament_Building%2C_Budapest%2C_outside.jpg/1280px-Parliament_Building%2C_Budapest%2C_outside.jpg"}
                        )

                    /*AsyncImage(
                        model = {"https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Parliament_Building%2C_Budapest%2C_outside.jpg/1280px-Parliament_Building%2C_Budapest%2C_outside.jpg"},
                        contentDescription = null
                    )*/
                    /*} else {
                        Image(
                            painter = painterResource(id = hu.autsoft.nytimes_gyulai_rainbowcake.R.drawable.baseline_circle_24),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }*/
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    "Országház",
                    maxLines = 1,
                    softWrap = true,
                    color = Color.Black
                )
                /*Text(
                    newsListItem.byline,
                    maxLines = 1,
                    softWrap = true,
                    color = colorResource(hu.autsoft.nytimes_gyulai_rainbowcake.R.color.secondary_text)
                )*/
                /*Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                   Icon(
                        tint = colorResource(hu.autsoft.nytimes_gyulai_rainbowcake.R.color.secondary_text),
                        modifier = Modifier.requiredSize(24.dp),
                        painter = painterResource(id = hu.autsoft.nytimes_gyulai_rainbowcake.R.drawable.baseline_event_24),
                        contentDescription = null
                    )
                    Text(
                        text = newsListItem.publishedDate,
                        color = colorResource(hu.autsoft.nytimes_gyulai_rainbowcake.R.color.secondary_text)
                    )
                }*/
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .requiredWidth(42.dp)
                    .fillMaxHeight(),
            ) {
                IconButton(
                    onClick = {onClicked()},
                    //modifier = Modifier.offset(x = 10.dp)
                ) {
                    Icon(imageVector  = Icons.Filled.Search, "")
                }
            }
        }
    }
}

/*Button(
content = { /*Text(getString(R.string.full_article), color = colorResource(R.color.button_text_color))*/ },
onClick = {

    if(isConnected(requireContext())) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(news?.url)
        startActivity(intent)
    }
    else
        Snackbar.make(requireView(), R.string.network_error, Snackbar.LENGTH_SHORT).show()
},
//colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.button_color))
)*/
