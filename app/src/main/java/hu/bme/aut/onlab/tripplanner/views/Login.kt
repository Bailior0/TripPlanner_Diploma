package hu.bme.aut.onlab.tripplanner.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.views.helpers.SegmentedControl

@Composable
fun Login(
    onLoginClick: (String, String, Context) -> Unit,
    onRegisterClick: (String, String, String, Context) -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var passInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }

    var switchState by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name)) },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 6.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .padding(top = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(140.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SegmentedControl(
                listOf(
                    stringResource(R.string.login),
                    stringResource(R.string.register)
                ),
                switchState
            ) { switchState = it }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
                label = { Text(stringResource(R.string.title_email)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.EmailAddress } // ← AUTOFILL!
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passInput,
                onValueChange = { passInput = it },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = { Icon(imageVector = Icons.Default.Key, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                label = { Text(stringResource(R.string.title_password)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.Password } // ← AUTOFILL!
            )

            if (switchState == 1) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text(stringResource(R.string.user_name_title)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    when {
                        emailInput.isEmpty() ->
                            Toast.makeText(context, R.string.missing_email, Toast.LENGTH_SHORT).show()

                        passInput.isEmpty() ->
                            Toast.makeText(context, R.string.missing_password, Toast.LENGTH_SHORT).show()

                        switchState == 0 ->
                            onLoginClick(emailInput, passInput, context)

                        switchState == 1 ->
                            onRegisterClick(nameInput, emailInput, passInput, context)
                    }
                }
            ) {
                Text(
                    text = if (switchState == 0) stringResource(R.string.login)
                    else stringResource(R.string.register),
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                )
            }
        }
    }
}
