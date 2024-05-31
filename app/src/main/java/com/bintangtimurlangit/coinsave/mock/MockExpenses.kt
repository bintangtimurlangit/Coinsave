package com.bintangtimurlangit.coinsave.mock

import androidx.compose.ui.graphics.Color
import com.bintangtimurlangit.coinsave.models.Category
import com.bintangtimurlangit.coinsave.models.Expense
import com.bintangtimurlangit.coinsave.models.Recurrence
import io.github.serpro69.kfaker.Faker
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

// This code generates mock data for categories and expenses using the Faker library.

// It creates a list of mock categories with random names and colors, and a list of mock expenses
// with random amounts, dates, recurrences, notes, and categories from the mock categories list.

val faker = Faker()

val mockCategories = listOf(
    Category(
        "Bills",
        Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        )
    ),
    Category(
        "Subscriptions", Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        )
    ),
    Category(
        "Take out", Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        )
    ),
    Category(
        "Hobbies", Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        )
    ),
)

val mockExpenses: List<Expense> = List(30) {
    Expense(
        amount = faker.random.nextInt(min = 1, max = 999)
            .toDouble() + faker.random.nextDouble(),
        date = LocalDateTime.now().minus(
            faker.random.nextInt(min = 300, max = 345600).toLong(),
            ChronoUnit.SECONDS
        ),
        recurrence = faker.random.randomValue(
            listOf(
                Recurrence.None,
                Recurrence.Daily,
                Recurrence.Monthly,
                Recurrence.Weekly,
                Recurrence.Yearly
            )
        ),
        note = faker.australia.animals(),
        category = faker.random.randomValue(mockCategories)
    )
}