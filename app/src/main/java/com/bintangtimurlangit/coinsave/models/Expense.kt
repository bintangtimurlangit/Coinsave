package com.bintangtimurlangit.coinsave.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

// Define the Expense class that extends RealmObject
class Expense() : RealmObject {
    // Define primary key
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var amount: Double = 0.0

    // Default recurrence name
    private var _recurrenceName: String = "None"
    // Recurrence property as Recurrence enum
    val recurrence: Recurrence get() { return _recurrenceName.toRecurrence() }

    // Default date value as current date and time
    private var _dateValue: String = LocalDateTime.now().toString()
    // Date property as LocalDateTime
    val date: LocalDateTime get() { return LocalDateTime.parse(_dateValue) }

    var note: String = ""
    var category: Category? = null

    // Secondary constructor to initialize all properties
    constructor(
        amount: Double,
        recurrence: Recurrence,
        date: LocalDateTime,
        note: String,
        category: Category,
    ) : this() {
        this.amount = amount
        this._recurrenceName = recurrence.name
        this._dateValue = date.toString()
        this.note = note
        this.category = category
    }
}

data class DayExpenses(
    val expenses: MutableList<Expense>,
    var total: Double,
)

// Group expenses by day
fun List<Expense>.groupedByDay(): Map<LocalDate, DayExpenses> {
    // Initialize a mutable map to store grouped data
    val dataMap: MutableMap<LocalDate, DayExpenses> = mutableMapOf()

    // Iterate over each expense
    this.forEach { expense ->
        // Extract the date from the expense
        val date = expense.date.toLocalDate()

        // Create a new entry in the map if it doesn't exist
        if (dataMap[date] == null) {
            dataMap[date] = DayExpenses(
                expenses = mutableListOf(),
                total = 0.0
            )
        }

        // Add the expense to the corresponding day's expenses list
        dataMap[date]!!.expenses.add(expense)
        // Update the total amount for the day
        dataMap[date]!!.total = dataMap[date]!!.total.plus(expense.amount)
    }

    // Sort expenses within each day by date
    dataMap.values.forEach { dayExpenses ->
        dayExpenses.expenses.sortBy { expense -> expense.date }
    }

    // Return the sorted map
    return dataMap.toSortedMap(compareByDescending { it })
}

// Group expenses by day of week
fun List<Expense>.groupedByDayOfWeek(): Map<String, DayExpenses> {
    // Initialize a mutable map to store grouped data
    val dataMap: MutableMap<String, DayExpenses> = mutableMapOf()

    // Iterate over each expense
    this.forEach { expense ->
        // Extract the day of week from the expense date
        val dayOfWeek = expense.date.toLocalDate().dayOfWeek

        // Create a new entry in the map if it doesn't exist
        if (dataMap[dayOfWeek.name] == null) {
            dataMap[dayOfWeek.name] = DayExpenses(
                expenses = mutableListOf(),
                total = 0.0
            )
        }

        // Add the expense to the corresponding day's expenses list
        dataMap[dayOfWeek.name]!!.expenses.add(expense)
        // Update the total amount for the day
        dataMap[dayOfWeek.name]!!.total = dataMap[dayOfWeek.name]!!.total.plus(expense.amount)
    }

    // Return the map
    return dataMap.toSortedMap(compareByDescending { it })
}

// Group expenses by day of month
fun List<Expense>.groupedByDayOfMonth(): Map<Int, DayExpenses> {
    // Initialize a mutable map to store grouped data
    val dataMap: MutableMap<Int, DayExpenses> = mutableMapOf()

    // Iterate over each expense
    this.forEach { expense ->
        // Extract the day of month from the expense date
        val dayOfMonth = expense.date.toLocalDate().dayOfMonth

        // Create a new entry in the map if it doesn't exist
        if (dataMap[dayOfMonth] == null) {
            dataMap[dayOfMonth] = DayExpenses(
                expenses = mutableListOf(),
                total = 0.0
            )
        }

        // Add the expense to the corresponding day's expenses list
        dataMap[dayOfMonth]!!.expenses.add(expense)
        // Update the total amount for the day
        dataMap[dayOfMonth]!!.total = dataMap[dayOfMonth]!!.total.plus(expense.amount)
    }

    // Return the map
    return dataMap.toSortedMap(compareByDescending { it })
}

// Group expenses by month
fun List<Expense>.groupedByMonth(): Map<String, DayExpenses> {
    // Initialize a mutable map to store grouped data
    val dataMap: MutableMap<String, DayExpenses> = mutableMapOf()

    // Iterate over each expense
    this.forEach { expense ->
        // Extract the month from the expense date
        val month = expense.date.toLocalDate().month

        // Create a new entry in the map if it doesn't exist
        if (dataMap[month.name] == null) {
            dataMap[month.name] = DayExpenses(
                expenses = mutableListOf(),
                total = 0.0
            )
        }

        // Add the expense to the corresponding month's expenses list
        dataMap[month.name]!!.expenses.add(expense)
        // Update the total amount for the month
        dataMap[month.name]!!.total = dataMap[month.name]!!.total.plus(expense.amount)
    }

    // Return the map
    return dataMap.toSortedMap(compareByDescending { it })
}