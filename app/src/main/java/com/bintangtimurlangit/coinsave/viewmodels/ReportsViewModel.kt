package com.bintangtimurlangit.coinsave.viewmodels

import androidx.lifecycle.ViewModel
import com.bintangtimurlangit.coinsave.models.Recurrence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Data class to represent the state of the Reports Screen
data class ReportsState(
  val recurrence: Recurrence = Recurrence.Weekly,
  val recurrenceMenuOpened: Boolean = false
)

class ReportsViewModel : ViewModel() {
  // MutableStateFlow to hold and manage UI state
  private val _uiState = MutableStateFlow(ReportsState())

  // Public StateFlow to expose the UI state
  val uiState: StateFlow<ReportsState> = _uiState.asStateFlow()

  // Function to set the recurrence in the UI state
  fun setRecurrence(recurrence: Recurrence) {
    _uiState.update { currentState ->
      currentState.copy(
        recurrence = recurrence
      )
    }
  }

  // Function to open the recurrence menu
  fun openRecurrenceMenu() {
    _uiState.update { currentState ->
      currentState.copy(
        recurrenceMenuOpened = true
      )
    }
  }

  // Function to close the recurrence menu
  fun closeRecurrenceMenu() {
    _uiState.update { currentState ->
      currentState.copy(
        recurrenceMenuOpened = false
      )
    }
  }
}
