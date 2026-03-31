package com.example.officereservationapp.veiw

import androidx.compose.ui.graphics.Color
import com.example.officereservationapp.model.Desk
import com.example.officereservationapp.ui.theme.AvailableDeskColor

sealed interface UserUiState {
    object Loading : UserUiState

    data class Success(
    val isSuccess: Boolean = true,
    val name: String = "",
    val reservedDeskId: Int? = null
    ): UserUiState

    object Error : UserUiState

}
