package com.bintangtimurlangit.coinsave.pages

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bintangtimurlangit.coinsave.components.PickerTrigger
import com.bintangtimurlangit.coinsave.components.expensesList.ExpensesList
import com.bintangtimurlangit.coinsave.models.Recurrence
import com.bintangtimurlangit.coinsave.ui.theme.CoinsaveTheme
import com.bintangtimurlangit.coinsave.ui.theme.LabelSecondary
import com.bintangtimurlangit.coinsave.ui.theme.TopAppBarBackground
import com.bintangtimurlangit.coinsave.ui.theme.Typography
import com.bintangtimurlangit.coinsave.viewmodels.ExpensesViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expenses(
  navController: NavController,
  vm: ExpensesViewModel = viewModel()
) {
  // List of recurrence options
  val recurrences = listOf(
    Recurrence.Daily,
    Recurrence.Weekly,
    Recurrence.Monthly,
    Recurrence.Yearly
  )

  // Collecting the UI state from the ViewModel
  val state by vm.uiState.collectAsState()
  // State for managing recurrence menu visibility
  var recurrenceMenuOpened by remember {
    mutableStateOf(false)
  }

  // Scaffold with top app bar and content
  Scaffold(
    topBar = {
      MediumTopAppBar(
        title = { Text("Expenses") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = TopAppBarBackground
        )
      )
    },
    content = { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(horizontal = 16.dp)
          .padding(top = 16.dp)
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Row for selecting recurrence
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            "Total for:",
            style = Typography.bodyMedium,
          )
          // Trigger for recurrence picker
          PickerTrigger(
            state.recurrence.target ?: Recurrence.None.target,
            onClick = { recurrenceMenuOpened = !recurrenceMenuOpened },
            modifier = Modifier.padding(start = 16.dp)
          )
          // Dropdown menu for selecting recurrence
          DropdownMenu(expanded = recurrenceMenuOpened,
            onDismissRequest = { recurrenceMenuOpened = false }) {
            recurrences.forEach { recurrence ->
              DropdownMenuItem(text = { Text(recurrence.target) }, onClick = {
                vm.setRecurrence(recurrence)
                recurrenceMenuOpened = false
              })
            }
          }
        }
        // Display total expenses amount
        Row(modifier = Modifier.padding(vertical = 32.dp)) {
          Text(
            "Rp",
            style = Typography.bodyMedium,
            color = LabelSecondary,
            modifier = Modifier.padding(end = 4.dp, top = 4.dp)
          )
          Text(
            DecimalFormat("0.#").format(state.sumTotal),
            style = Typography.titleLarge
          )
        }
        // Display list of expenses
        ExpensesList(
          expenses = state.expenses,
          modifier = Modifier
            .weight(1f)
            .verticalScroll(
              rememberScrollState()
            )
        )
      }
    }
  )
}

// Preview for Expenses composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ExpensesPreview() {
  CoinsaveTheme {
    Expenses(navController = rememberNavController())
  }
}
