package com.example.officereservationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.officereservationapp.ui.theme.OfficeReservationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfficeReservationAppTheme {
                MyApplication()
            }
        }
    }
}


//@Composable
//fun MyNavigation() {
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    OfficeReservationAppTheme {
//        MainPage(navController)
//    }
//}