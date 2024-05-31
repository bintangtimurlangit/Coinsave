package com.bintangtimurlangit.coinsave.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintangtimurlangit.coinsave.db
import com.bintangtimurlangit.coinsave.models.Category
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Data class to represent the state of the Categories Screen
data class CategoriesState(
  val newCategoryColor: Color = Color.White,
  val newCategoryName: String = "",
  val colorPickerShowing: Boolean = false,
  val categories: List<Category> = listOf()
)

class CategoriesViewModel : ViewModel() {
  // MutableStateFlow to hold and manage UI state
  private val _uiState = MutableStateFlow(CategoriesState())

  // Public StateFlow to expose the UI state
  val uiState: StateFlow<CategoriesState> = _uiState.asStateFlow()

  init {
    // Initialize the UI state with the categories from the database
    _uiState.update { currentState ->
      currentState.copy(
        categories = db.query<Category>().find()
      )
    }

    // Listen for changes in categories and update the UI state accordingly
    viewModelScope.launch(Dispatchers.IO) {
      db.query<Category>().asFlow().collect { changes ->
        _uiState.update { currentState ->
          currentState.copy(
            categories = changes.list
          )
        }
      }
    }
  }

  // Function to set the new category color
  fun setNewCategoryColor(color: Color) {
    _uiState.update { currentState ->
      currentState.copy(
        newCategoryColor = color
      )
    }
  }

  // Function to set the new category name
  fun setNewCategoryName(name: String) {
    _uiState.update { currentState ->
      currentState.copy(
        newCategoryName = name
      )
    }
  }

  // Function to show the color picker
  fun showColorPicker() {
    _uiState.update { currentState ->
      currentState.copy(
        colorPickerShowing = true
      )
    }
  }

  // Function to hide the color picker
  fun hideColorPicker() {
    _uiState.update { currentState ->
      currentState.copy(
        colorPickerShowing = false
      )
    }
  }

  // Function to create a new category
  fun createNewCategory() {
    viewModelScope.launch(Dispatchers.IO) {
      db.write {
        this.copyToRealm(Category(
          _uiState.value.newCategoryName,
          _uiState.value.newCategoryColor
        ))
      }
      _uiState.update { currentState ->
        currentState.copy(
          newCategoryColor = Color.White,
          newCategoryName = ""
        )
      }
    }
  }

  // Function to delete a category
  fun deleteCategory(category: Category) {
    viewModelScope.launch(Dispatchers.IO) {
      db.write {
        val deletingCategory = this.query<Category>("_id == $0", category._id).find().first()
        delete(deletingCategory)
      }
    }
  }
}
