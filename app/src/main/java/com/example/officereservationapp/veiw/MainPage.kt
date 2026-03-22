package com.example.officereservationapp.veiw

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.loader.content.Loader
import com.example.officereservationapp.R
import com.example.officereservationapp.viewmodel.MyViewModel
import com.example.officereservationapp.ui.theme.*
import kotlin.text.isNotEmpty


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavHostController, viewModel: MyViewModel) {

    val uiSate by viewModel.uiState.collectAsState()


    val dropDownStatus = viewModel.dropDownStatus
    val selectedOffice = viewModel.selectedOffice
    val scrollState = rememberScrollState()
    val smallSpacerSize = viewModel.smallSpacerSize
    val mediumSpacerSize = viewModel.mediumSpacerSize
    val largeSpacerSize = viewModel.largeSpacerSize


    val focusManager = LocalFocusManager.current
    val myContext = LocalContext.current


    when (val state = uiSate) {
        is DeskUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is DeskUiState.Success -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.baseline_apartment),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .background(
                                            color = Primary,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .size(45.dp)
                                        .padding(all = 5.dp)
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Column {
                                    Text(
                                        text = "Deskly",
                                        color = Color.White,
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Secondary
                        ),

                        )
                },
                content = { paddingValue ->
                    Row(
                        modifier = Modifier
                            .padding(paddingValue)
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    dropDownStatus.value = false
                                    focusManager.clearFocus()
                                }
                            )
                            .background(Background),
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(horizontal = 40.dp, vertical = 40.dp)
                                .verticalScroll(scrollState)
                                .background(
                                    color = Surface,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 40.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(largeSpacerSize))
                            Image(
                                painter = painterResource(R.drawable.baseline_apartment),
                                contentDescription = "",
                                modifier = Modifier
                                    .background(
                                        color = Primary,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .size(80.dp)
                                    .padding(all = 15.dp)
                            )
                            Spacer(modifier = Modifier.height(mediumSpacerSize))
                            Text(
                                text = "Office Reservation",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 35.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(smallSpacerSize))

                            Text(
                                text = "Reserve your workspace for a specific date",
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp,
                            )
                            Spacer(modifier = Modifier.height(mediumSpacerSize))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Your Name",
                                    textAlign = TextAlign.Start,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                )

                                Spacer(modifier = Modifier.height(smallSpacerSize))

                                TextField(
                                    value = viewModel.name.value,
                                    onValueChange = { viewModel.updateName(it) },
                                    label = { Text(text = "Enter your full name") },
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
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Log.d("DEBUG", "UserName in Main Page: ${viewModel.name.value}")
                                Spacer(modifier = Modifier.height(mediumSpacerSize))
                                Text(
                                    text = "Select Office",
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
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text(text = "Select an office") },
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


                            }
                            Spacer(modifier = Modifier.height(mediumSpacerSize))

                            ElevatedButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary
                                ),
                                shape = RoundedCornerShape(16.dp),
                                //enabled = viewModel.mainPageButtonIsClickable(userName.value, selectedOffice.value),
                                //This line above you can comment or not depending on what type of functionality you want

                                onClick = {
                                    if (viewModel.name.value.isNotEmpty() && selectedOffice.value.isNotEmpty())
                                    /*
                                    (viewModel.mainPageButtonIsClickable
                                    (
                                    viewModel.name.value,
                                    selectedOffice.value
                                    )
                                    )
                                    */
                                        navController.navigate("ReservationPage")
                                    else {
                                        Toast.makeText(
                                            myContext,
                                            "Please fill in all fields",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                },

                                ) {
                                Text(text = "Continue")
                            }
                            Spacer(modifier = Modifier.height(largeSpacerSize))

                        }


                    }
                }
            )
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





