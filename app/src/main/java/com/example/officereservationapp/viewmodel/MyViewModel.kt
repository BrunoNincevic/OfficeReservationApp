package com.example.officereservationapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officereservationapp.repository.DeskRepository
import com.example.officereservationapp.repository.UserRepository
import com.example.officereservationapp.veiw.DeskUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MyViewModel : ViewModel() {

    private val _deskUiState = MutableStateFlow<DeskUiState>(DeskUiState.Success(isSuccess = true))
    val deskUiState = _deskUiState.asStateFlow()

    private val _userUiState = MutableStateFlow<DeskUiState>(DeskUiState.Success(isSuccess = true))

    val userUiState = _userUiState.asStateFlow()

    var name = mutableStateOf("")
        private set
    var userId = mutableStateOf("")
        private set
    var email = mutableStateOf("")
        private set
    var password = mutableStateOf("")
        private set
    var passwordCheck = mutableStateOf("")
        private set
    var dropDownStatus = mutableStateOf(false)
        private set
    var selectedOffice = mutableStateOf("")
        private set
    var userReservedDeskId = mutableStateOf("")
        private set
    var smallSpacerSize = 5.dp
    var mediumSpacerSize = 20.dp
    var largeSpacerSize = 30.dp

    val deskRepository = DeskRepository()
    val userRepository = UserRepository()


    private val _errorEvents = Channel<String>()
    val errorEvents = _errorEvents.receiveAsFlow()


    fun fetchDesks() {
        // Prevent unnecessary Loading states if we already have data
        val currentState = _deskUiState.value
        val hasDesks = currentState is DeskUiState.Success && currentState.desks.isNotEmpty()

        viewModelScope.launch {
            if (!hasDesks) {
                _deskUiState.update { DeskUiState.Loading }
            }

            if (!deskRepository.checkIfDesksExist()) {
                deskRepository.createInitialDesksInDatabase()
            }

            deskRepository.getDesks().collect { desksFetched ->
                _deskUiState.update { state ->
                    if (state is DeskUiState.Success) {
                        state.copy(desks = desksFetched)
                    } else {
                        DeskUiState.Success(desks = desksFetched, isSuccess = true)
                    }
                }
            }
        }
    }

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updatePasswordCheck(newPasswordCheck: String) {
        passwordCheck.value = newPasswordCheck
    }

    fun allRegistrationConditionsMatched(): Boolean {
        return name.value.isNotEmpty() &&
                email.value.isNotEmpty() &&
                password.value.isNotEmpty() &&
                (password.value == passwordCheck.value)
    }

    fun registerUser() {
        viewModelScope.launch {
            val existingDesks =
                if (_deskUiState.value is DeskUiState.Success) (_deskUiState.value as DeskUiState.Success).desks else emptyList()
            _deskUiState.update { DeskUiState.Loading }

            val result = userRepository.registerNewUser(
                name.value,
                email.value,
                password.value,
                selectedOffice.value
            )
            result.fold(
                onSuccess = {
                    userId.value =
                        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    fetchDeskReservedByUser(userId.value)
                    _deskUiState.update {
                        DeskUiState.Success(
                            desks = existingDesks,
                            isSuccess = true
                        )
                    }
                },
                onFailure = { exception ->
                    _errorEvents.send(exception.message ?: "Registration Failed")
                    _deskUiState.update {
                        DeskUiState.Success(
                            desks = existingDesks,
                            isSuccess = false
                        )
                    }
                }
            )
        }
    }

    fun loginUser() {
        viewModelScope.launch {
            val existingDesks =
                if (_deskUiState.value is DeskUiState.Success) (_deskUiState.value as DeskUiState.Success).desks else emptyList()
            _deskUiState.update { DeskUiState.Loading }

            val result = userRepository.loginUser(email.value, password.value)

            result.fold(
                onSuccess = {
                    val uid =
                        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    userId.value = uid

                    fetchDeskReservedByUser(userId.value)

                    val fetchedName = userRepository.fetchUsername(uid)
                    name.value = fetchedName

                    Log.d("DEBUG", "Login success. Fetched name: $fetchedName")

                    _deskUiState.update {
                        DeskUiState.Success(
                            desks = existingDesks,
                            isSuccess = true
                        )
                    }
                },
                onFailure = { exception ->
                    _errorEvents.send(exception.message ?: "Login failed")
                    _deskUiState.update {
                        DeskUiState.Success(
                            desks = existingDesks,
                            isSuccess = false
                        )
                    }
                }
            )
        }
    }

    fun fetchDeskReservedByUser(userId: String) {
        viewModelScope.launch {
            val fetchedDeskReservedByUser = userRepository.fetchDeskId(userId)
            userReservedDeskId.value = fetchedDeskReservedByUser

        }
    }

    fun changeSelectedDesk(deskId: Int?) {
        _deskUiState.update { currentState ->
            if (currentState is DeskUiState.Success) {
                currentState.copy(selectedDeskId = deskId)
            } else {
                currentState
            }
        }
    }

    fun reserveDesk(selectedDeskId: Int?, userId: String, userName: String) {
        deskRepository.reserveDesk(selectedDeskId, userId, userName)

    }

    fun unreserveDesk(selectedDeskId: Int?, userId: String) {
        deskRepository.unreserveDesk(selectedDeskId, userId)

    }


    fun logoutUser() {
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
        userId.value = ""
        name.value = ""
        email.value = ""
        password.value = ""
        // Reset UI state so navigation doesn't get triggered automatically
        _deskUiState.update { DeskUiState.Success(isSuccess = false) }

    }
}
