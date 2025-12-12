package hu.bme.aut.onlab.tripplanner.views

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.tasks.await

@Composable
fun Share(
    posts: List<SharedData>,
    user: String?,
    onAddCommentClick: () -> Unit,
    onEditClicked: (SharedData) -> Unit,
    onDeleteClicked: (SharedData) -> Unit,
    onLikeClicked: (SharedData) -> Unit
) {
    val storage = Firebase.storage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(12.dp, 10.dp, 12.dp, 10.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (
                button,
                list
            ) = createRefs()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 5.dp)
                    .constrainAs(list) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {
                itemsIndexed(posts.sortedByDescending { it.liked.size }) { _, item ->
                    ListItem(
                        item = item,
                        user = user,
                        storage = storage,
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked,
                        onLikeClicked = onLikeClicked
                    )
                }
            }

            FloatingActionButton(
                onClick = onAddCommentClick,
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(Icons.Filled.Share,"")
            }
        }
    }
}

@Composable
fun ListItem(
    item: SharedData,
    user: String?,
    storage: FirebaseStorage,
    onEditClicked: (SharedData) -> Unit,
    onDeleteClicked: (SharedData) -> Unit,
    onLikeClicked: (SharedData) -> Unit
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    if (item.pic != null && item.pic!!.isNotEmpty()) {
        val path = item.pic!!
        val imageRef = storage.reference.child(path)

        LaunchedEffect(path) {
            try {
                imageUrl = imageRef.downloadUrl.await().toString()
            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = 6.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFBEE3F8), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.nickname?.take(1)?.uppercase() ?: "?",
                        fontSize = 20.sp,
                        color = Color(0xFF1A365D),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.nickname ?: "Ismeretlen",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = item.title ?: "",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.body ?: "",
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (imageUrl != null && !isLoading) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                IconButton(onClick = { onLikeClicked(item) }) {
                    if (item.liked.contains(user)) {
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Liked",
                            tint = Color(0xFF1E88E5)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "Like"
                        )
                    }
                }

                Text(
                    text = item.liked.size.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.liked.contains(user)) Color(0xFF1E88E5) else MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = stringResource(R.string.useful),
                    fontSize = 14.sp,
                    color = if (item.liked.contains(user)) Color(0xFF1E88E5) else Color.Gray
                )
            }

            if (item.uid == user) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { onEditClicked(item) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDeleteClicked(item) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
