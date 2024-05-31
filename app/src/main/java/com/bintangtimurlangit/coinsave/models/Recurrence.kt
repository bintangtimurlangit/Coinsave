package com.bintangtimurlangit.coinsave.models

// Define a sealed class for Recurrence
sealed class Recurrence(val name: String, val target: String) {
    object None : Recurrence("None", "None")
    object Daily : Recurrence("Daily", "Today")
    object Weekly : Recurrence("Weekly", "This Week")
    object Monthly : Recurrence("Monthly", "This Month")
    object Yearly : Recurrence("Yearly", "This Year")
}

// Extension function to convert a string to Recurrence
fun String.toRecurrence(): Recurrence {
    return when(this) {
        "None" -> Recurrence.None
        "Daily" -> Recurrence.Daily
        "Weekly" -> Recurrence.Weekly
        "Monthly" -> Recurrence.Monthly
        "Yearly" -> Recurrence.Yearly
        else -> Recurrence.None
    }
}