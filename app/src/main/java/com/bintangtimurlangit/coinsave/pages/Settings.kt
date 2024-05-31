package com.bintangtimurlangit.coinsave.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.bintangtimurlangit.coinsave.components.TableRow
import com.bintangtimurlangit.coinsave.db
import com.bintangtimurlangit.coinsave.models.Category
import com.bintangtimurlangit.coinsave.models.Expense
import com.bintangtimurlangit.coinsave.ui.theme.BackgroundElevated
import com.bintangtimurlangit.coinsave.ui.theme.DividerColor
import com.bintangtimurlangit.coinsave.ui.theme.Shapes
import com.bintangtimurlangit.coinsave.ui.theme.TopAppBarBackground
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController) {
  // Remembering the coroutine scope
  val coroutineScope = rememberCoroutineScope()
  // State for showing delete confirmation dialog
  var deleteConfirmationShowing by remember {
    mutableStateOf(false)
  }

  // Function to erase all data
  val eraseAllData: () -> Unit = {
    coroutineScope.launch {
      // Delete all expenses and categories from the database
      db.write {
        val expenses = this.query<Expense>().find()
        val categories = this.query<Category>().find()

        delete(expenses)
        delete(categories)

        // Dismiss the delete confirmation dialog
        deleteConfirmationShowing = false
      }
    }
  }

  // Scaffold with top app bar and content
  Scaffold(
    topBar = {
      MediumTopAppBar(
        title = { Text("Settings") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = TopAppBarBackground
        )
      )
    },
    content = { innerPadding ->
      Column(modifier = Modifier.padding(innerPadding)) {
        Column(
          modifier = Modifier
            .padding(16.dp)
            .clip(Shapes.large)
            .background(BackgroundElevated)
            .fillMaxWidth()
        ) {
          // Settings options
          TableRow(
            label = "Categories",
            hasArrow = true,
            modifier = Modifier.clickable {
              navController.navigate("settings/categories")
            }
          )
          Divider(
            modifier = Modifier
              .padding(start = 16.dp), thickness = 1.dp, color = DividerColor
          )
          TableRow(
            label = "Erase all data",
            isDestructive = true,
            modifier = Modifier.clickable {
              // Show delete confirmation dialog on click
              deleteConfirmationShowing = true
            }
          )

          // Delete confirmation dialog
          if (deleteConfirmationShowing) {
            AlertDialog(
              onDismissRequest = { deleteConfirmationShowing = false },
              title = { Text("Are you sure?") },
              text = { Text("This action cannot be undone.") },
              confirmButton = {
                TextButton(onClick = eraseAllData) {
                  Text("Delete everything")
                }
              },
              dismissButton = {
                TextButton(onClick = { deleteConfirmationShowing = false }) {
                  Text("Cancel")
                }
              }
            )
          }
        }
      }
    }
  )
}