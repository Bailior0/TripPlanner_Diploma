package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import hu.bme.aut.onlab.tripplanner.R

@Composable
fun Login(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String) -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var passInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            TopAppBar(
                title = { Text(text = "TripPlanner") }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp, 25.dp, 12.dp, 100.dp)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val (
                        icon,
                        email,
                        password,
                        loginButton,
                        registerButton
                    ) = createRefs()

                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier
                            //.fillMaxSize()
                            .size(128.dp)
                            .constrainAs(icon) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        singleLine = true,
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
                            .constrainAs(email) {
                                top.linkTo(icon.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
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
                            .constrainAs(password) {
                                top.linkTo(email.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Button(
                        content = {
                            Text(
                                text = "LOGIN",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(25.dp, 0.dp)
                            )
                        },
                        onClick = {
                            onLoginClick(emailInput, passInput)
                        },
                        modifier = Modifier
                            .padding(0.dp, 25.dp, 0.dp, 0.dp)
                            .constrainAs(loginButton) {
                                top.linkTo(password.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Button(
                        content = {
                            Text(
                                text = "REGISTER",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(25.dp, 0.dp)
                            )
                        },
                        onClick = {
                            onRegisterClick(emailInput, passInput)
                        },
                        modifier = Modifier
                            .padding(0.dp, 5.dp, 0.dp, 0.dp)
                            .constrainAs(registerButton) {
                                top.linkTo(loginButton.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }
            }
        }

}