package com.example.officereservationapp.veiw

import androidx.compose.ui.graphics.Color
import com.example.officereservationapp.model.Desk
import com.example.officereservationapp.ui.theme.AvailableDeskColor

sealed interface DeskUiState {
    object Loading : DeskUiState

    data class Success(
    val isSuccess: Boolean = true,
    val desks: List<Desk> = emptyList(),
    val name: String = "",
    val selectedOffice: String = "",
    val selectedDeskId: Int? = null
    ): DeskUiState

    object Error : DeskUiState

}
