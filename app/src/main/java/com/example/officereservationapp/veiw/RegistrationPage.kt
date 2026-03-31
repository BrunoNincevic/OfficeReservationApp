package com.example.officereservationapp.veiw

import android.widget.Toast
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officereservationapp.R
import com.example.officereservationapp.viewmodel.MyViewModel
import com.example.officereservationapp.ui.theme.*
import kotlin.text.isNotEmpty
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.input.pointer.pointerInput


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPage(navController: NavHostController, viewModel: MyViewModel) {

    val uiSate by viewModel.deskUiState.collectAsState()
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val dropDownStatus = viewModel.dropDownStatus
    val selectedOffice = viewModel.selectedOffice
    val scrollState = rememberScrollState()
    val smallSpacerSize = viewModel.smallSpacerSize
    val mediumSpacerSize = viewModel.mediumSpacerSize
    val largeSpacerSize = viewModel.largeSpacerSize
    val focusManager = LocalFocusManager.current

    val myContext = LocalContext.current

    LaunchedEffect(viewModel.errorEvents) {
        viewModel.errorEvents.collect { message ->
            Toast.makeText(myContext, message, Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(uiSate) {
        val state = uiSate
        if (state is DeskUiState.Success && state.isSuccess && viewModel.userId.value.isNotEmpty()) {
            navController.navigate("ReservationPage") {
                popUpTo("RegistrationPage") { inclusive = true } // Clear registration from backstack
            }
        }
    }
    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }


    when (val state = uiSate) {
        is DeskUiState.Loading -> {
            LoadingScreen()
        }

        is DeskUiState.Success -> {
            if (state.isSuccess && viewModel.userId.value.isNotEmpty()) {
                LoadingScreen()
            }
            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Background)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                focusManager.clearFocus()
                            }
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .background(color = Secondary),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.baseline_apartment),
                            contentDescription = "",
                            modifier = Modifier
                                .background(
                                    color = Primary,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .size(75.dp)
                                .padding(all = 5.dp)
                        )
                        Spacer(modifier = Modifier.height(mediumSpacerSize))
                        Text(
                            text = "Create Account",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Join OfficeBook to start reserving",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 30.dp, vertical = 30.dp)
                            .background(
                                color = Surface,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(scrollState)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        focusManager.clearFocus()
                                    }
                                }
                                .padding(top = 30.dp, bottom = 20.dp)
                        ) {

                            Text(
                                text = "Registration",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = smallSpacerSize),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                text = "Enter your credentials to create account",
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Full Name",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(smallSpacerSize))

                            TextField(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.PersonOutline,
                                        contentDescription = null
                                    )
                                },
                                value = viewModel.name.value,
                                onValueChange = {
                                    viewModel.updateName(it)
                                },
                                placeholder = {
                                    Text(text = "John Smith",
                                        maxLines = 1)
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.LightGray,
                                    unfocusedContainerColor = Color.LightGray,
                                    unfocusedLabelColor = Color.DarkGray,
                                    focusedLabelColor = Color.DarkGray,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent

                                ), shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(mediumSpacerSize))
                            Text(
                                text = "Department",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(smallSpacerSize))
                            ExposedDropdownMenuBox(
                                expanded = dropDownStatus.value,
                                onExpandedChange = {
                                    dropDownStatus.value = !dropDownStatus.value
                                }
                            ) {
                                TextField(
                                    value = selectedOffice.value,
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Business,
                                            contentDescription = null
                                        )
                                    },
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text(text = "Select an office") },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.LightGray,
                                        unfocusedContainerColor = Color.LightGray,
                                        unfocusedLabelColor = Color.DarkGray,
                                        focusedLabelColor = Color.DarkGray,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = dropDownStatus.value
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()

                                )
                                ExposedDropdownMenu(
                                    expanded = dropDownStatus.value,
                                    onDismissRequest = { dropDownStatus.value = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Casino Natives") },
                                        onClick = {
                                            dropDownStatus.value = false
                                            selectedOffice.value = "Casino Natives"
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "Web") },
                                        onClick = {
                                            dropDownStatus.value = false
                                            selectedOffice.value = "Web"
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "Configuration") },
                                        onClick = {
                                            dropDownStatus.value = false
                                            selectedOffice.value = "Configuration"
                                        }
                                    )

                                }
                            }

                            Spacer(modifier = Modifier.height(mediumSpacerSize))


                            Text(
                                text = "Email Address",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(smallSpacerSize))

                            TextField(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.MailOutline,
                                        contentDescription = null
                                    )
                                },
                                value = viewModel.email.value,
                                onValueChange = {
                                    viewModel.updateEmail(it)
                                },
                                placeholder = {
                                    Text(text = "your.email@company.com",
                                        maxLines = 1)
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.LightGray,
                                    unfocusedContainerColor = Color.LightGray,
                                    unfocusedLabelColor = Color.DarkGray,
                                    focusedLabelColor = Color.DarkGray,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent

                                ), shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(mediumSpacerSize))
                            Text(
                                text = "Password",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(smallSpacerSize))


                            TextField(
                                value = viewModel.password.value,
                                onValueChange = {
                                    viewModel.updatePassword(it)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null
                                    )
                                },
                                visualTransformation = if (passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        passwordVisible = !passwordVisible
                                    }) {
                                        Icon(
                                            imageVector = if (passwordVisible)
                                                Icons.Default.Visibility
                                            else
                                                Icons.Default.VisibilityOff,
                                            contentDescription = null
                                        )
                                    }
                                },

                                placeholder = {
                                    Text(text = "Enter your Password",
                                        maxLines = 1)
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.LightGray,
                                    unfocusedContainerColor = Color.LightGray,
                                    unfocusedLabelColor = Color.DarkGray,
                                    focusedLabelColor = Color.DarkGray,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent

                                ), shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(mediumSpacerSize))
                            Text(
                                text = "Confirm Password",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(smallSpacerSize))


                            TextField(
                                value = viewModel.passwordCheck.value,
                                onValueChange = {
                                    viewModel.updatePasswordCheck(it)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null
                                    )
                                },
                                visualTransformation = if (passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        passwordVisible = !passwordVisible
                                    }) {
                                        Icon(
                                            imageVector = if (passwordVisible)
                                                Icons.Default.Visibility
                                            else
                                                Icons.Default.VisibilityOff,
                                            contentDescription = null
                                        )
                                    }
                                },

                                placeholder = {
                                    Text(text = "Re-enter your Password",
                                        maxLines = 1)
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.LightGray,
                                    unfocusedContainerColor = Color.LightGray,
                                    unfocusedLabelColor = Color.DarkGray,
                                    focusedLabelColor = Color.DarkGray,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent

                                ), shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.width(mediumSpacerSize))
                        }


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ) {
                            ElevatedButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary
                                ),
                                shape = RoundedCornerShape(16.dp),
                                enabled = (viewModel.name.value != "" && viewModel.email.value != "" && viewModel.password.value != "" && viewModel.passwordCheck.value != ""),
                                //This line above you can comment or not depending on what type of functionality you want

                                onClick = {
                                    if (viewModel.allRegistrationConditionsMatched()) {
                                        viewModel.registerUser()
                                        //navController.navigate("ReservationPage")
                                    } else {
                                        Toast.makeText(
                                            myContext,
                                            "Please fill in all fields with valid information",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                },

                                ) {
                                Text(text = "Create Account")

                            }
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = smallSpacerSize)
                            ) {

                                Text(
                                    text = "Already have an account? ",
                                    fontSize = 15.sp
                                )

                                Text(
                                    text = "Sign in",
                                    textAlign = TextAlign.End,
                                    color = Primary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            enabled = true,
                                            onClick = {
                                                navController.navigate("LoginPage"){
                                                    popUpTo("RegistrationPage") { inclusive = true } // Clear registration from backstack

                                                }
                                            }
                                        )
                                )
                            }


                        }
                    }


                }
            }

        }

        is DeskUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Shit happened")
            }

        }
    }
}







