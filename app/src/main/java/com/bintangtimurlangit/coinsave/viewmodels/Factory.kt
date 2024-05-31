package com.bintangtimurlangit.coinsave.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Function to create a ViewModelProvider.Factory for ViewModels
inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
  object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
  }
