package com.example.officereservationapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.officereservationapp.ui.theme.OfficeReservationAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.officereservationapp.veiw.MainPage
import com.example.officereservationapp.veiw.ReservationPage
import com.example.officereservationapp.veiw.TitlePage
import com.example.officereservationapp.viewmodel.MyViewModel


@Preview
@Composable
fun MyApplication() {
    OfficeReservationAppTheme {

        val navController = rememberNavController()
        val sharedViewModel: MyViewModel = viewModel()


        NavHost(
            navController = navController,
            startDestination = "TitlePage"
        ) {
            composable ("TitlePage") {
                TitlePage(navController)
            }

            composable("MainPage") {
                MainPage(navController, sharedViewModel)
            }

            composable("ReservationPage") {
                ReservationPage(navController, sharedViewModel)
            }

        }
    }
}

