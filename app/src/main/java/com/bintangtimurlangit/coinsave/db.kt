package com.bintangtimurlangit.coinsave

import com.bintangtimurlangit.coinsave.models.Category
import com.bintangtimurlangit.coinsave.models.Expense
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

// Configuration for Realm database with specified schema classes (Category and Expense)
val config = RealmConfiguration.create(schema = setOf(Category::class, Expense::class))

// Opening a Realm database instance with the above configuration
val db: Realm = Realm.open(config)