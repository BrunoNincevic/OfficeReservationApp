package com.example.officereservationapp.veiw

import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officereservationapp.ui.theme.Tertiary
import com.example.officereservationapp.viewmodel.MyViewModel
import com.example.officereservationapp.ui.theme.*
import com.example.officereservationapp.model.Desk
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationPage(navController: NavHostController, viewModel: MyViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchDesks()
    }
    LaunchedEffect(viewModel.userId.value) {
        if (viewModel.userId.value.isEmpty()) {
            navController.navigate("LoginPage") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val uiSate by viewModel.deskUiState.collectAsState()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val myContext = LocalContext.current

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Logout") },
            text = { Text(text = "Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logoutUser()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Choose Your Desk",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                val currentDate = remember {
                                    SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date())
                                }
                                Text(
                                    text = currentDate,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                showLogoutDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )

                },
                content = { paddingValue ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValue)
                            .width(screenWidth)
                            .height(screenHeight)
                            .background(Background)
                    ) {
                        state.desks.forEach { desk ->
                            val backgroundColor = when {
                                desk.id == state.selectedDeskId -> SelectedDeskColor
                                desk.reservedByUserId?.isNotEmpty() == true && desk.reservedByUserId == viewModel.userId.value -> UserReservedDeskColor
                                desk.reservedByUserId?.isNotEmpty() == true -> TakenDeskColor
                                else -> AvailableDeskColor
                            }
                            key(desk.id) {
                                Box(
                                    modifier = Modifier
                                        .offset(
                                            x = screenWidth * desk.xCoordinatePercentage,
                                            y = screenHeight * desk.yCoordinatePercentage
                                        )
                                        .shadow(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(
                                            color = backgroundColor,
                                            shape = RoundedCornerShape(16.dp),
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = Tertiary,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                            onClick = {
                                                if (!desk.reservedByUserId.isNullOrEmpty() && desk.reservedByUserId != viewModel.userId.value) {
                                                    Toast.makeText(
                                                        myContext,
                                                        "This desk is already taken",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else if (state.selectedDeskId != null && desk.id == state.selectedDeskId) {
                                                    viewModel.changeSelectedDesk(null)
                                                } else if(desk.reservedByUserId.isNullOrEmpty() && viewModel.userReservedDeskId.value != "" && desk.reservedByUserId != viewModel.userId.value){
                                                    Toast.makeText(
                                                        myContext,
                                                        "You can reserve only one desk per day",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                else {
                                                    viewModel.changeSelectedDesk(desk.id)
                                                }
                                            })
                                        .size(screenWidth * 0.2f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val textToShow = when {
                                        desk.reservedByUserId.isNullOrEmpty() -> "Desk ${desk.id}"
                                        desk.reservedByUserId == viewModel.userId.value -> "Your Desk"
                                        else -> "Reserved by\n${desk.reservedByUserName ?: "User"}"
                                    }

                                    Text(
                                        text = textToShow,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        fontSize = if (desk.reservedByUserId.isNullOrEmpty()) 14.sp else 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 13.sp,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.height(150.dp),
                        containerColor = Color.White,
                        content = {
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(bottom = 10.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    BottomStatusDisplay(
                                        desks = state.desks,
                                        selectedDeskId = state.selectedDeskId,
                                        currentUserId = viewModel.userId.value
                                    )
                                }

                                Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                                    ElevatedButton(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Primary
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        enabled = state.selectedDeskId != null,
                                        onClick = {
                                            if (state.selectedDeskId != null) {
                                                if (state.selectedDeskId.toString() == viewModel.userReservedDeskId.value) {
                                                    // It's the user's own desk -> Unreserve
                                                    viewModel.unreserveDesk(state.selectedDeskId, viewModel.userId.value)
                                                    viewModel.fetchDeskReservedByUser(viewModel.userId.value) // Refresh local state
                                                    viewModel.changeSelectedDesk(null)
                                                } else {
                                                    // It's a new desk -> Reserve (The UI click logic should already prevent selecting others' desks)
                                                    viewModel.reserveDesk(state.selectedDeskId, viewModel.userId.value, viewModel.name.value)
                                                    viewModel.fetchDeskReservedByUser(viewModel.userId.value)
                                                    viewModel.changeSelectedDesk(null)
                                                }
                                            }
                                            else
                                                Toast.makeText(
                                                    myContext,
                                                    "Please select a desk",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                        },


                                        ) {
                                        if (state.selectedDeskId != null && state.selectedDeskId.toString() != viewModel.userReservedDeskId.value)
                                            Text(text = "Reserve Desk ${state.selectedDeskId}")

                                        else if (state.selectedDeskId != null && state.selectedDeskId.toString() == viewModel.userReservedDeskId.value)
                                            Text(text = "Free up Desk ${state.selectedDeskId}")
                                        else
                                            Text(text = "Please Select a Desk")
                                    }
                                }
                            }
                        }
                    )
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

@Composable
fun BottomStatusDisplay(
    desks: List<Desk>,
    selectedDeskId: Int?,
    currentUserId: String
) {
    val availableCount = desks.count { it.reservedByUserId.isNullOrEmpty() && it.id != selectedDeskId }
    val takenCount = desks.count { !it.reservedByUserId.isNullOrEmpty() && it.reservedByUserId != currentUserId }

    val userReservedDesk = desks.find { it.reservedByUserId == currentUserId }
    val reservedValue = if (userReservedDesk != null) "Desk ${userReservedDesk.id}" else "-"
    val selectedText = if (selectedDeskId != null) "Desk $selectedDeskId" else "-"

    StatusItem(value = availableCount.toString(), label = "Available", color = AvailableDeskColor)
    StatusItem(value = takenCount.toString(), label = "Taken", color = TakenDeskColor)
    StatusItem(value = selectedText, label = "Selected", color = SelectedDeskColor)
    StatusItem(value = reservedValue, label = "Reserved", color = UserReservedDeskColor)
}

@Composable
private fun StatusItem(value: String, label: String, color: Color) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
