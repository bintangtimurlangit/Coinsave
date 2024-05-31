package com.bintangtimurlangit.coinsave.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintangtimurlangit.coinsave.db
import com.bintangtimurlangit.coinsave.models.Expense
import com.bintangtimurlangit.coinsave.models.Recurrence
import com.bintangtimurlangit.coinsave.utils.calculateDateRange
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class to represent the state of the Expenses Screen
data class ExpensesState(
  val recurrence: Recurrence = Recurrence.Daily,
  val sumTotal: Double = 1250.98,
  val expenses: List<Expense> = listOf()
)

class ExpensesViewModel: ViewModel() {
  // MutableStateFlow to hold and manage UI state
  private val _uiState = MutableStateFlow(ExpensesState())

  // Public StateFlow to expose the UI state
  val uiState: StateFlow<ExpensesState> = _uiState.asStateFlow()

  init {
    // Initialize the UI state with the expenses from the database
    _uiState.update { currentState ->
      currentState.copy(
        expenses = db.query<Expense>().find()
      )
    }

    // Set the default recurrence on initialization
    viewModelScope.launch(Dispatchers.IO) {
      setRecurrence(Recurrence.Daily)
    }
  }

  // Function to set the recurrence and update the UI state accordingly
  fun setRecurrence(recurrence: Recurrence) {
    // Calculate the date range based on the recurrence
    val (start, end) = calculateDateRange(recurrence, 0)

    // Filter expenses based on the calculated date range
    val filteredExpenses = db.query<Expense>().find().filter { expense ->
      (expense.date.toLocalDate().isAfter(start) && expense.date.toLocalDate()
        .isBefore(end)) || expense.date.toLocalDate()
        .isEqual(start) || expense.date.toLocalDate().isEqual(end)
    }

    // Calculate the total sum of expenses within the date range
    val sumTotal = filteredExpenses.sumOf { it.amount }

    // Update the UI state with the new recurrence, filtered expenses, and total sum
    _uiState.update { currentState ->
      currentState.copy(
        recurrence = recurrence,
        expenses = filteredExpenses,
        sumTotal = sumTotal
      )
    }
  }
}
