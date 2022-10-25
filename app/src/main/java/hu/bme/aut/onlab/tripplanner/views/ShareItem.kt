package hu.bme.aut.onlab.tripplanner.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData

@Composable
fun ShareItem(
    item: SharedData?,
    onOkUploadClick: (String, String, String) -> Unit,
    onOkEditClick: (SharedData) -> Unit,
    onCancelClick: () -> Unit
) {
    var nickInput by remember { mutableStateOf("") }
    var titleInput by remember { mutableStateOf("") }
    var commentInput by remember { mutableStateOf("") }

    if(item != null) {
        if(item.nickname != null && item.title != null && item.body != null) {
            nickInput = item.nickname.toString()
            titleInput = item.title.toString()
            commentInput = item.body.toString()
        }
    }

    val context = LocalContext.current
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
                okButton
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
                        top.linkTo(comment.bottom)
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
                            onOkUploadClick(nickInput, titleInput, commentInput)
                    } else {
                        if(nickInput.isEmpty() || titleInput.isEmpty() || commentInput.isEmpty())
                            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                        else {
                            item.nickname = nickInput
                            item.title = titleInput
                            item.body = commentInput
                            onOkEditClick(item)
                        }
                    }
                },
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(okButton) {
                        top.linkTo(comment.bottom)
                        end.linkTo(cancelButton.start)
                    }
            )
        }
    }
}