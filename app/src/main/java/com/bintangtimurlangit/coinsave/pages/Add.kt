package com.bintangtimurlangit.coinsave.pages

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.bintangtimurlangit.coinsave.components.TableRow
import com.bintangtimurlangit.coinsave.components.UnstyledTextField
import com.bintangtimurlangit.coinsave.models.Recurrence
import com.bintangtimurlangit.coinsave.ui.theme.*
import com.bintangtimurlangit.coinsave.viewmodels.AddViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Add(navController: NavController, vm: AddViewModel = viewModel()) {
  // Collecting the UI state from the ViewModel
  val state by vm.uiState.collectAsState()

  // List of recurrence options
  val recurrences = listOf(
    Recurrence.None,
    Recurrence.Daily,
    Recurrence.Weekly,
    Recurrence.Monthly,
    Recurrence.Yearly
  )

  // Scaffold with a top app bar and content
  Scaffold(topBar = {
    MediumTopAppBar(
      title = { Text("Add") },
      colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = TopAppBarBackground
      )
    )
  }, content = { innerPadding ->
    // Column layout for the content
    Column(
      modifier = Modifier.padding(innerPadding),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Card-like container for input fields
      Column(
        modifier = Modifier
          .padding(16.dp)
          .clip(Shapes.large)
          .background(BackgroundElevated)
          .fillMaxWidth()
      ) {
        // Input field for amount
        TableRow(label = "Amount", detailContent = {
          UnstyledTextField(
            value = state.amount,
            onValueChange = vm::setAmount,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0") },
            arrangement = Arrangement.End,
            maxLines = 1,
            textStyle = TextStyle(
              textAlign = TextAlign.Right,
            ),
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Number,
            )
          )
        })
        // Divider
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )
        // Input field for recurrence
        TableRow(label = "Recurrence", detailContent = {
          var recurrenceMenuOpened by remember {
            mutableStateOf(false)
          }
          TextButton(
            onClick = { recurrenceMenuOpened = true }, shape = Shapes.large
          ) {
            Text(state.recurrence?.name ?: Recurrence.None.name)
            DropdownMenu(expanded = recurrenceMenuOpened,
              onDismissRequest = { recurrenceMenuOpened = false }) {
              recurrences.forEach { recurrence ->
                DropdownMenuItem(text = { Text(recurrence.name) }, onClick = {
                  vm.setRecurrence(recurrence)
                  recurrenceMenuOpened = false
                })
              }
            }
          }
        })
        // Divider
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )
        // Date picker
        var datePickerShowing by remember {
          mutableStateOf(false)
        }
        TableRow(label = "Date", detailContent = {
          TextButton(onClick = { datePickerShowing = true }) {
            Text(state.date.toString())
          }
          if (datePickerShowing) {
            DatePickerDialog(onDismissRequest = { datePickerShowing = false },
              onDateChange = { it ->
                vm.setDate(it)
                datePickerShowing = false
              },
              initialDate = state.date,
              title = { Text("Select date", style = Typography.titleLarge) })
          }
        })
        // Divider
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )
        // Input field for note
        TableRow(label = "Note", detailContent = {
          UnstyledTextField(
            value = state.note,
            placeholder = { Text("Leave some notes") },
            arrangement = Arrangement.End,
            onValueChange = vm::setNote,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
              textAlign = TextAlign.Right,
            ),
          )
        })
        // Divider
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )
        // Input field for category
        TableRow(label = "Category", detailContent = {
          var categoriesMenuOpened by remember {
            mutableStateOf(false)
          }
          TextButton(
            onClick = { categoriesMenuOpened = true }, shape = Shapes.large
          ) {
            Text(
              state.category?.name ?: "Select a category first",
              color = state.category?.color ?: Color.White
            )
            DropdownMenu(expanded = categoriesMenuOpened,
              onDismissRequest = { categoriesMenuOpened = false }) {
              state.categories?.forEach { category ->
                DropdownMenuItem(text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                      modifier = Modifier.size(10.dp),
                      shape = CircleShape,
                      color = category.color
                    ) {}
                    Text(
                      category.name, modifier = Modifier.padding(start = 8.dp)
                    )
                  }
                }, onClick = {
                  vm.setCategory(category)
                  categoriesMenuOpened = false
                })
              }
            }
          }
        })
      }
      // Submit button
      Button(
        onClick = vm::submitExpense,
        modifier = Modifier.padding(16.dp),
        shape = Shapes.large,
        enabled = state.category != null
      ) {
        Text("Submit expense")
      }
    }
  })
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewAdd() {
  CoinsaveTheme {
    val navController = rememberNavController()
    Add(navController = navController)
  }
}
