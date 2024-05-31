package com.bintangtimurlangit.coinsave.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintangtimurlangit.coinsave.db
import com.bintangtimurlangit.coinsave.models.Category
import com.bintangtimurlangit.coinsave.models.Expense
import com.bintangtimurlangit.coinsave.models.Recurrence
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Currency

// Data class to represent the state of the Add Screen
data class AddScreenState(
  val amount: String = "",
  val recurrence: Recurrence = Recurrence.None,
  val date: LocalDate = LocalDate.now(),
  val note: String = "",
  val category: Category? = null,
  val categories: RealmResults<Category>? = null
)

class AddViewModel : ViewModel() {
  // MutableStateFlow to hold and manage UI state
  private val _uiState = MutableStateFlow(AddScreenState())

  // Public StateFlow to expose the UI state
  val uiState: StateFlow<AddScreenState> = _uiState.asStateFlow()

  init {
    // Initialize the UI state with the categories from the database
    _uiState.update { currentState ->
      currentState.copy(
        categories = db.query<Category>().find()
      )
    }
  }

  // Function to set the amount
  fun setAmount(amount: String) {
    var parsed = amount.toDoubleOrNull()

    if (amount.isEmpty()) {
      parsed = 0.0
    }

    if (parsed != null) {
      _uiState.update { currentState ->
        currentState.copy(
          amount = amount.trim().ifEmpty { "0" },
        )
      }
    }
  }

  // Function to set the recurrence
  fun setRecurrence(recurrence: Recurrence) {
    _uiState.update { currentState ->
      currentState.copy(
        recurrence = recurrence,
      )
    }
  }

  // Function to set the date
  fun setDate(date: LocalDate) {
    _uiState.update { currentState ->
      currentState.copy(
        date = date,
      )
    }
  }

  // Function to set the note
  fun setNote(note: String) {
    _uiState.update { currentState ->
      currentState.copy(
        note = note,
      )
    }
  }

  // Function to set the category
  fun setCategory(category: Category) {
    _uiState.update { currentState ->
      currentState.copy(
        category = category,
      )
    }
  }

  // Function to submit the expense
  fun submitExpense() {
    if (_uiState.value.category != null) {
      viewModelScope.launch(Dispatchers.IO) {
        val now = LocalDateTime.now()

        // Write the expense to the database
        db.write {
          this.copyToRealm(
            Expense(
              _uiState.value.amount.toDouble(),
              _uiState.value.recurrence,
              _uiState.value.date.atTime(now.hour, now.minute, now.second),
              _uiState.value.note,
              this.query<Category>("_id == $0", _uiState.value.category!!._id)
                .find().first(),
            )
          )
        }

        // Reset the UI state after submitting the expense
        _uiState.update { currentState ->
          currentState.copy(
            amount = "",
            recurrence = Recurrence.None,
            date = LocalDate.now(),
            note = "",
            category = null,
            categories = null
          )
        }
      }
    }
  }
}