package hu.bme.aut.onlab.tripplanner.views

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun AuthChange(
    onOkClick: (String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    var passInput by remember { mutableStateOf("") }
    var mailInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
                pass,
                mail,
                cancelButton,
                okButton
            ) = createRefs()

            Text(
                text = "Change email",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(5.dp, 0.dp)
                    .constrainAs(dialogTitle) {
                        top.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )

            OutlinedTextField(
                value = passInput,
                onValueChange = { passInput = it },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(imageVector  = Icons.Filled.Key, "mail")
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
                label = {
                    Text(
                        text = "Password",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(pass) {
                        top.linkTo(dialogTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            OutlinedTextField(
                value = mailInput,
                onValueChange = { mailInput = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(imageVector  = Icons.Filled.Email, "mail")
                },
                label = {
                    Text(
                        text = "Email",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(mail) {
                        top.linkTo(pass.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Button(
                content = {
                    Text(
                        text = "CANCEL",
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
                        top.linkTo(mail.bottom)
                        end.linkTo(parent.end)
                    }
            )

            Button(
                content = {
                    Text(
                        text = "OK",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(20.dp, 0.dp)
                    )
                },
                onClick = {
                    if(passInput.isEmpty() || mailInput.isEmpty())
                        Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                    else
                        onOkClick(passInput, mailInput)
                },
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(okButton) {
                        top.linkTo(mail.bottom)
                        end.linkTo(cancelButton.start)
                    }
            )
        }
    }
}