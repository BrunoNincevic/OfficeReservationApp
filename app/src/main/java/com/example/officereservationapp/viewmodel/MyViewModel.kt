package com.example.officereservationapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officereservationapp.model.Desk
import com.example.officereservationapp.repository.DeskRepository
import com.example.officereservationapp.ui.theme.*
import com.example.officereservationapp.veiw.DeskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DeskUiState>(DeskUiState.Success(isSuccess = true))
    val uiState = _uiState.asStateFlow()

    var name = mutableStateOf("")
        private set
    var dropDownStatus = mutableStateOf(false)
        private set
    var selectedOffice = mutableStateOf("")
        private set

    //    var buttonIsClickable = mutableStateOf(false)
//        private set
//    var selectedDesk = mutableStateOf<Int?>(null)
//        private set
    var desks by mutableStateOf<List<Desk>>(emptyList())
        private set
    var smallSpacerSize = 5.dp
    var mediumSpacerSize = 20.dp
    var largeSpacerSize = 30.dp

    val repository = DeskRepository()


    fun fetchDesks() {
        viewModelScope.launch {
            _uiState.update { DeskUiState.Loading }
            if(!repository.checkIfDesksExist()){
                repository.createInitialDesksInDatabase()
            }
            repository.getDesks().collect { desksFetched ->
                _uiState.update { currentState ->
                if (currentState is DeskUiState.Success) {
                    currentState.copy(desks = desksFetched)
                } else {
                    DeskUiState.Success(desks = desksFetched)
                }
            }
            }
        }
    }

    /*
    suspend fun getDesksFromRepository(): List<Desk> {
    repository.getDesks { desksFetched ->
    desks = desksFetched

    }
    Log.d("DEBUG", "Desks in VM: $desks")
    return desks
    }
    */


    /*
    init {
    createDesksOnScreen()
    }
    fun createDesksOnScreen() {
    var deskIdCounter = 1

    for (row in 1..4) {
    for (column in 1..3) {
    if ((row == 2 && column == 1) || (row == 3 && column == 1))
    continue
    else {
    desks.add(
    Desk(
    deskIdCounter,
    ((2.5 * column) / 10).toFloat(),
    ((1.3 * row) / 10).toFloat()
    )
    )
    }
    deskIdCounter++
    }
    }
    desks[3].isReservedBy = "bruno"
    }
    */

    fun updateName(newName: String) {
        Log.d("DEBUG", "NewName in ViewModel: ${newName}")
        name.value = newName
        Log.d("DEBUG", "Name in ViewModel: ${name.value}")
    }

    /*
    fun mainPageButtonIsClickable(name: String, selectedOffice: String): Boolean {
    if (name.isNotEmpty() && selectedOffice.isNotEmpty()) {
    buttonIsClickable.value = true
    } else {
    buttonIsClickable.value = false
    }
    return buttonIsClickable.value

    }
    */

    fun changeSelectedDesk(deskId: Int?) {
        _uiState.update { currentState ->
            // Check if the current state is Success before trying to copy it
            if (currentState is DeskUiState.Success) {
                currentState.copy(selectedDeskId = deskId)
            } else {
                // If it wasn't Success (e.g. Loading), this logic shouldn't really
                // trigger, but we return the state as-is just in case.
                currentState
            }
        }
    }
    /*
    fun deskColor(selectedDeskId: Int?, desk: Desk) {
    _uiState.value = DeskUiState.Loading
    try {


    Log.d("DEBUG", "Name in ViewModel: ${name.value}")
    if (desk.id == selectedDeskId) {
    _uiState.value = DeskUiState.Success(deskColor=SelectedDeskColor)
    } else if (desk.reservedByUser != "" && desk.reservedByUser != name.value) {
    _uiState.value = DeskUiState.Success(deskColor=TakenDeskColor)
    } else if (desk.reservedByUser == name.value) {
    _uiState.value = DeskUiState.Success(deskColor=UserReservedDeskColor)
    } else {
    _uiState.value = DeskUiState.Success(deskColor=AvailableDeskColor)
    }
    }
    catch (e: Exception){
    _uiState.value = DeskUiState.Error
    }
    }
    */

    fun reserveDesk(selectedDeskId: Int) {
        Log.d("DEBUG", "proslo je za desk ${selectedDeskId}")
        repository.reserveDesk(selectedDeskId, name.value)
    }


}
