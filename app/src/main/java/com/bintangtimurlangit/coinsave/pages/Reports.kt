package com.bintangtimurlangit.coinsave.pages

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.bintangtimurlangit.coinsave.R
import com.bintangtimurlangit.coinsave.components.ReportPage
import com.bintangtimurlangit.coinsave.models.Recurrence
import com.bintangtimurlangit.coinsave.ui.theme.TopAppBarBackground
import com.bintangtimurlangit.coinsave.viewmodels.ReportsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun Reports(vm: ReportsViewModel = viewModel()) {
  // Collecting the UI state from the ViewModel
  val uiState = vm.uiState.collectAsState().value

  // List of recurrence options
  val recurrences = listOf(
    Recurrence.Weekly,
    Recurrence.Monthly,
    Recurrence.Yearly
  )

  // Scaffold with top app bar and content
  Scaffold(
    topBar = {
      MediumTopAppBar(
        title = { Text("Reports") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = TopAppBarBackground
        ),
        actions = {
          // IconButton for opening recurrence menu
          IconButton(onClick = vm::openRecurrenceMenu) {
            Icon(
              painterResource(id = R.drawable.ic_today),
              contentDescription = "Change recurrence"
            )
          }
          // Dropdown menu for selecting recurrence
          DropdownMenu(
            expanded = uiState.recurrenceMenuOpened,
            onDismissRequest = vm::closeRecurrenceMenu
          ) {
            recurrences.forEach { recurrence ->
              DropdownMenuItem(text = { Text(recurrence.name) }, onClick = {
                vm.setRecurrence(recurrence)
                vm.closeRecurrenceMenu()
              })
            }
          }
        }
      )
    },
    content = { innerPadding ->
      // Calculate number of pages based on recurrence
      val numOfPages = when (uiState.recurrence) {
        Recurrence.Weekly -> 53
        Recurrence.Monthly -> 12
        Recurrence.Yearly -> 1
        else -> 53 // Default to weekly
      }
      // HorizontalPager for navigating between report pages
      HorizontalPager(count = numOfPages, reverseLayout = true) { page ->
        ReportPage(innerPadding, page, uiState.recurrence)
      }
    }
  )
}
