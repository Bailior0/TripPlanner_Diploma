package hu.bme.aut.onlab.tripplanner.views

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.tasks.await


@Composable
fun ShareItem(
    item: SharedData?,
    onOkUploadClick: (String, String, String, Bitmap?) -> Unit,
    onOkEditClick: (SharedData, Bitmap?) -> Unit,
    onCancelClick: () -> Unit
) {
    var nickInput by remember { mutableStateOf("") }
    var titleInput by remember { mutableStateOf("") }
    var commentInput by remember { mutableStateOf("") }
    var mutableImage by remember { mutableStateOf<Bitmap?>(null) }

    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val storage = Firebase.storage
    val context = LocalContext.current

    if(item != null && item.pic != null && item.pic != "") {
        val storagePath = item.pic
        val imageRef = storage.reference.child(item.pic!!)

        LaunchedEffect(storagePath) {
            try {
                imageUrl = imageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if(uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
            bitmap?.let {  btm ->
                mutableImage = btm
            }
        }
    }

    if(item != null) {
        if(item.nickname != null && item.title != null && item.body != null) {
            nickInput = item.nickname.toString()
            titleInput = item.title.toString()
            commentInput = item.body.toString()
        }
    }
    val maxChar = 20

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (
                dialogTitle,
                nick,
                title,
                comment,
                cancelButton,
                okButton,
                imageConst
            ) = createRefs()

            Text(
                text = when(item == null) {
                    true -> stringResource(R.string.create_comment)
                    false -> stringResource(R.string.edit_comment)
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(5.dp, 0.dp)
                    .constrainAs(dialogTitle) {
                        top.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(nick) {
                        top.linkTo(dialogTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = nickInput,
                    onValueChange = {
                        if (it.length <= maxChar)
                            nickInput = it
                    },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(R.string.nick),
                            color = Color.Gray
                        )
                    },
                )
                Text(
                    text = "${nickInput.length} / $maxChar",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(title) {
                        top.linkTo(nick.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = titleInput,
                    onValueChange = {
                        if (it.length <= maxChar)
                            titleInput = it
                    },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(R.string.title),
                            color = Color.Gray
                        )
                    },
                )
                Text(
                    text = "${titleInput.length} / $maxChar",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                )
            }

            OutlinedTextField(
                value = commentInput,
                onValueChange = { commentInput = it },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(R.string.post),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(comment) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Box(
                modifier = Modifier
                    .clickable { launcher.launch("image/*") }
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(imageConst) {
                        top.linkTo(comment.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                if (mutableImage != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        Image(
                            bitmap = mutableImage!!.asImageBitmap(),
                            contentDescription =null,
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.CenterVertically)
                                .clickable { launcher.launch("image/*") }
                        )
                        Icon(
                            imageVector  = Icons.Outlined.Clear,
                            null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { mutableImage = null }
                        )
                    }

                } else if(imageUrl != null && !isLoading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        imageUrl?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = "Image",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickable { launcher.launch("image/*") }
                            )
                        }
                        Icon(
                            imageVector  = Icons.Outlined.Clear,
                            null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { mutableImage = null; imageUrl = null }
                        )
                    }
                } else {
                    Icon(
                        imageVector  = Icons.Outlined.PhotoLibrary,
                        null,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { launcher.launch("image/*") }
                    )
                }
            }

            Button(
                content = {
                    Text(
                        text = stringResource(R.string.button_cancel),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(20.dp, 0.dp)
                    )
                },
                onClick = {
                    onCancelClick()
                },
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(cancelButton) {
                        top.linkTo(imageConst.bottom)
                        end.linkTo(parent.end)
                    }
            )

            Button(
                content = {
                    Text(
                        text = stringResource(R.string.button_ok),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(20.dp, 0.dp)
                    )
                },
                onClick = {
                    if(item == null) {
                        if(nickInput.isEmpty() || titleInput.isEmpty() || commentInput.isEmpty())
                            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                        else
                            onOkUploadClick(nickInput, titleInput, commentInput, mutableImage)
                    } else {
                        if(nickInput.isEmpty() || titleInput.isEmpty() || commentInput.isEmpty())
                            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                        else {
                            item.nickname = nickInput
                            item.title = titleInput
                            item.body = commentInput
                            onOkEditClick(item, mutableImage)
                        }
                    }
                },
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(okButton) {
                        top.linkTo(imageConst.bottom)
                        end.linkTo(cancelButton.start)
                    }
            )
        }
    }
}