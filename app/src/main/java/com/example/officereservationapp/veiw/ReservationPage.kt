package com.example.officereservationapp.veiw

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officereservationapp.ui.theme.Tertiary
import com.example.officereservationapp.viewmodel.MyViewModel
import com.example.officereservationapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationPage(navController: NavHostController, viewModel: MyViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchDesks()
    }
    val uiSate by viewModel.uiState.collectAsState()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
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
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                                viewModel.changeSelectedDesk(null)
                            }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                            }
                        },
                        title = {
                            Text(text = "Choose Your Desk")
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
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .background(Background)
                            .height(screenHeight)
                    ) {
                        state.desks.forEach { desk ->
                            val backgroundColor = when {
                                desk.id == state.selectedDeskId -> SelectedDeskColor
                                desk.reservedByUser?.isNotEmpty() == true && desk.reservedByUser == viewModel.name.value -> UserReservedDeskColor
                                desk.reservedByUser?.isNotEmpty() == true -> {
                                    TakenDeskColor
                                }

                                else -> AvailableDeskColor
                            }
                            key(desk.id) {
                                Log.d("DEBUG", "Desk ${desk.id} is in reservartion page")
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
//                                    color = when{
//                                        desk.reservedByUser == "" && (desk.id != selectedDesk.value) -> AvailableDeskColor
//                                        desk.reservedByUser == viewModel.name.value -> UserReservedDeskColor
//                                        desk.reservedByUser != "" && desk.reservedByUser != viewModel.name.value -> TakenDeskColor
//                                        desk.id == selectedDesk.value -> SelectedDeskColor
//                                        else -> AvailableDeskColor
//
//
//                                    },
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
                                                if (desk.reservedByUser != "") {
                                                    Toast.makeText(
                                                        myContext,
                                                        "Please select unreserved desk",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }

                                                //selectedDesk.value = desk.id
                                                else if (state.selectedDeskId != null && desk.id == state.selectedDeskId)
                                                    viewModel.changeSelectedDesk(null)
                                                else
                                                    viewModel.changeSelectedDesk(desk.id)


                                            })
                                        .size(screenWidth * 0.2f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text =
                                            if (desk.reservedByUser == "")
                                                "Desk ${desk.id}"
                                            else
                                                "${desk.reservedByUser}",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                },
                bottomBar = {

                    BottomAppBar(
                        modifier = Modifier
                            .height(height = 150.dp),
                        containerColor = Color.White,
                        content = {
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {

/*
state.desks.forEach { desk ->

}
Column(
verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally
) {
Text(
text = "numberOfAvailableDesks",
color = AvailableDeskColor,
fontWeight = FontWeight.Bold,
fontSize = 20.sp
)
Text(
text = "Available",
fontSize = 12.sp,
fontWeight = FontWeight.SemiBold
)
}
Column(
verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally
) {
Text(
text = "Number of not available desks",
color = TakenDeskColor,
fontWeight = FontWeight.Bold,
fontSize = 20.sp
)
Text(
text = "Taken",
fontSize = 12.sp,
fontWeight = FontWeight.SemiBold
)
}
Column(
verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally
) {

Text(

text = "Desk selected",
color = SelectedDeskColor,
fontWeight = FontWeight.Bold,
fontSize = 20.sp
)
Text(
text = "Selected",
fontSize = 12.sp,
fontWeight = FontWeight.SemiBold
)
}
Text(
text = "reserved desk number",
color = UserReservedDeskColor,
fontWeight = FontWeight.Bold,
fontSize = 20.sp
)
Text(
text = "Reserved",
fontSize = 12.sp,
fontWeight = FontWeight.SemiBold
)
}


}
}


}
*/
                                    Row() {
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
                                                    state.desks.forEach { desk ->
                                                        if (desk.id == state.selectedDeskId) {
                                                            // viewModel.deskReservedByThisUserId=state.selectedDeskId
                                                            viewModel.reserveDesk(state.selectedDeskId!!)
                                                            viewModel.changeSelectedDesk(null)

                                                        }
                                                    }

                                                } else
                                                    Toast.makeText(
                                                        myContext,
                                                        "Please select a desk",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                            },


                                            ) {
                                            if (state.selectedDeskId != null)
                                                Text(text = "Reserve Desk ${state.selectedDeskId}")
                                            else
                                                Text(text = "Please Select a Desk")
                                        }
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


