package hu.bme.aut.onlab.tripplanner.views

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun Identifier(
    prediction: String,
    onImageChosen: (Bitmap, Context) -> Unit
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    var bitmap :Bitmap? = null
    var loadedImage by remember {
        mutableStateOf(true)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        loadedImage = false
    }
    Column() {
        Button(
            onClick = {
                launcher.launch("image/*")

            }
        ) {
            Text(text = "Pick image")
        }

        imageUri?.let {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
            bitmap?.let {  btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier.size(400.dp)
                )
            }
        }

        Text(text = prediction)
    }
    if(!loadedImage) {
        bitmap?.let {  btm ->
            onImageChosen(btm, context)
            loadedImage = true
        }
    }

}